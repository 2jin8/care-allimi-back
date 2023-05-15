package kr.ac.kumoh.allimi.controller;

import jakarta.validation.Valid;
import kr.ac.kumoh.allimi.dto.notice.NoticeEditDto;
import kr.ac.kumoh.allimi.dto.notice.NoticeListDTO;
import kr.ac.kumoh.allimi.controller.response.NoticeResponse;
import kr.ac.kumoh.allimi.dto.notice.NoticeWriteDto;
import kr.ac.kumoh.allimi.exception.FacilityException;
import kr.ac.kumoh.allimi.exception.NHResidentException;
import kr.ac.kumoh.allimi.exception.NoticeException;
import kr.ac.kumoh.allimi.exception.user.UserAuthException;
import kr.ac.kumoh.allimi.exception.user.UserException;
import kr.ac.kumoh.allimi.service.NoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
@RestController
public class NoticeController { 
  private final NoticeService noticeService;
  
  // 알림장 작성 
  @PostMapping(value = "/v2/notices")  // notice{user_id, nhresident_id, facility_id, contents, sub_contents}, file{}
  public ResponseEntity noticeWrite(@Valid @RequestPart(value="notice") NoticeWriteDto dto,
                                    @RequestPart(value="file", required = false) List<MultipartFile> files) throws Exception {

    Long noticeId;

    try {
      noticeId = noticeService.write(dto, files);
    } catch (UserAuthException e) { //알림장 쓸 권한 없음
      log.info("NoticeController 알림장 작성: 권한이 없는 사용자");
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    } catch (UserException |NHResidentException |FacilityException e) { //알림장 쓰기 실패
      log.info("NoticeController 알림장 작성: user, resident, facility 중 하나 찾기 실패");
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    } catch (Exception e) { //알림장 쓰기 실패
      log.info("NoticeController 알림장 작성: 알림장 쓰기 실패");
      System.out.println(e.getMessage());
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    Map<String, Long> map = new HashMap<>();
    map.put("notice_id", noticeId);

    System.out.println(noticeId);
    return ResponseEntity.status(HttpStatus.OK).body(map);
  }

  @GetMapping("/v2/notices/{resident_id}") // 알림장 목록
  public ResponseEntity noticeList(@PathVariable("resident_id") Long residentId) {
    if (residentId == null)
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

    List<NoticeListDTO> noticeList;

    try {
      noticeList = noticeService.noticeList(residentId);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    return ResponseEntity.status(HttpStatus.OK).body(noticeList);
  }

  @GetMapping("/v2/notices/detail/{notice_id}") // 알림장 상세보기
  public ResponseEntity noticeDetail(@PathVariable("notice_id") Long noticeId) {
    if (noticeId == null)
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

    NoticeResponse noticeResponse;

    try {
      noticeResponse = noticeService.getDetail(noticeId);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    return ResponseEntity.status(HttpStatus.ACCEPTED).body(noticeResponse);
  }

  @PatchMapping("/v2/notices") // 알림장 수정
  public ResponseEntity noticeEdit(@RequestPart(value = "notice") NoticeEditDto dto,
                                   @RequestPart(value = "file", required = false) List<MultipartFile> files) {

    if (dto.getNotice_id() == null || dto.getUser_id() == null || dto.getResident_id() == null)
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

    Long noticeId = null;

    try {
      noticeId = noticeService.edit(dto, files);
    } catch (NoticeException e) {
      log.info("NoticeController 알림장 수정: 해당하는 알림장이 없음");
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    } catch (UserException e) {
      log.info("NoticeController 알림장 수정: 해당하는 user가 없음");
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    } catch (UserAuthException e) {
      log.info("NoticeController 알림장 수정: 권한이 없는 사용자");
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    } catch (NHResidentException e) {
      log.info("NoticeController 알림장 수정: 해당하는 입소자가 없음");
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    } catch (Exception e) {
      log.info("NoticeController 알림장 수정: 기타 예외");
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    Map<String, Long> map = new HashMap<>();
    map.put("notice_id", noticeId);

    return ResponseEntity.status(HttpStatus.OK).body(map);
  }

  @DeleteMapping("/v2/notices") // 알림장 삭제
  public ResponseEntity noticeDelete(@RequestBody Map<String, Long> notice) {
    Long noticeId = notice.get("notice_id");

    if (noticeId == null)
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

    Long deletedCnt = noticeService.delete(noticeId);

    if (deletedCnt == 0)
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

    return ResponseEntity.status(HttpStatus.OK).build();
  }
}


