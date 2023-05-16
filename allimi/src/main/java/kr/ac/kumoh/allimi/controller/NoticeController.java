package kr.ac.kumoh.allimi.controller;

import jakarta.validation.Valid;
import kr.ac.kumoh.allimi.dto.notice.NoticeEditDto;
import kr.ac.kumoh.allimi.dto.notice.NoticeListDTO;
import kr.ac.kumoh.allimi.controller.response.NoticeResponse;
import kr.ac.kumoh.allimi.dto.notice.NoticeWriteDto;
import kr.ac.kumoh.allimi.exception.NHResidentException;
import kr.ac.kumoh.allimi.exception.NoticeException;
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

    Long noticeId = noticeService.write(dto, files);
    Map<String, Long> map = new HashMap<>();
    map.put("notice_id", noticeId);

    return ResponseEntity.status(HttpStatus.OK).body(map);
  }

  @GetMapping("/v2/notices/{resident_id}") // 알림장 목록
  public ResponseEntity noticeList(@PathVariable("resident_id") Long residentId) throws Exception {
    if (residentId == null)
      throw new NHResidentException("NoticeController 알림장 목록: resident_id가 null. 사용자 입력이 잘못됨");

    List<NoticeListDTO> noticeList = noticeService.noticeList(residentId);

    return ResponseEntity.status(HttpStatus.OK).body(noticeList);
  }

  @GetMapping("/v2/notices/detail/{notice_id}") // 알림장 상세보기
  public ResponseEntity noticeDetail(@PathVariable("notice_id") Long noticeId) throws Exception {
    if (noticeId == null)
      throw new NoticeException("NoticeController 알림장 상세보기: resident_id가 null. 사용자 입력이 잘못됨");

    NoticeResponse noticeResponse = noticeService.getDetail(noticeId);

    return ResponseEntity.status(HttpStatus.ACCEPTED).body(noticeResponse);
  }

  @PatchMapping("/v2/notices") // 알림장 수정
  public ResponseEntity noticeEdit(@Valid @RequestPart(value = "notice") NoticeEditDto dto,
                                   @RequestPart(value = "file", required = false) List<MultipartFile> files) throws Exception {

    Long noticeId = noticeService.edit(dto, files);
    Map<String, Long> map = new HashMap<>();
    map.put("notice_id", noticeId);

    return ResponseEntity.status(HttpStatus.OK).body(map);
  }

  @DeleteMapping("/v2/notices") // 알림장 삭제
  public ResponseEntity noticeDelete(@RequestBody Map<String, Long> notice) {
    Long noticeId = notice.get("notice_id");
    if (noticeId == null)
      throw new NoticeException("NoticeController 알림장 삭제: notice_id가 null. 사용자 입력이 잘못됨");

    Long deletedCnt = noticeService.delete(noticeId);
    if (deletedCnt == 0)
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

    return ResponseEntity.status(HttpStatus.OK).build();
  }
}


