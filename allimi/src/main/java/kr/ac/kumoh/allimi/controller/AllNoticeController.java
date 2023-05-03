package kr.ac.kumoh.allimi.controller;

import kr.ac.kumoh.allimi.controller.response.AllNoticeResponse;
import kr.ac.kumoh.allimi.dto.allNotice.AllNoticeEditDto;
import kr.ac.kumoh.allimi.dto.allNotice.AllNoticeListDTO;
import kr.ac.kumoh.allimi.dto.allNotice.AllNoticeWriteDto;
import kr.ac.kumoh.allimi.controller.response.NoticeResponse;
import kr.ac.kumoh.allimi.dto.notice.NoticeEditDto;
import kr.ac.kumoh.allimi.exception.AllNoticeException;
import kr.ac.kumoh.allimi.exception.FacilityException;
import kr.ac.kumoh.allimi.exception.NHResidentException;
import kr.ac.kumoh.allimi.exception.user.UserAuthException;
import kr.ac.kumoh.allimi.exception.user.UserException;
import kr.ac.kumoh.allimi.service.AllNoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
@RestController
public class AllNoticeController {
    private final AllNoticeService allNoticeService;

  // 전체 공지사항 작성
  @PostMapping(value = "/v2/all-notices")  // allnotice{user_id, facility_id, title, contents, important}, file{}
  public ResponseEntity allNoticeWrite(@RequestPart(value="allnotice") AllNoticeWriteDto dto,
                                    @RequestPart(value="file", required = false) List<MultipartFile> files) {

    if (dto.getUser_id() == null || dto.getFacility_id() == null || dto.getTitle() == null) {
      log.info("NoticeController 알림장 작성: 필요한  값이 제대로 안들어옴. 사용자의 잘못된 입력");
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    try {
      allNoticeService.write(dto, files);
    } catch (UserAuthException e) {
      log.info("AllNoticeController 전체공지 작성: 권한이 없는 사용자");
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    } catch (UserException | NHResidentException | FacilityException e) {
      log.info("AllNoticeController 전체공지 작성: user, resident, facility 중 하나 찾기 실패");
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    } catch (Exception e) {
      log.info("AllNoticeController 전체공지 작성: 쓰기 실패");
      System.out.println(e.getMessage());
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @GetMapping("/v2/all-notices/{facility_id}") // 전체공지 목록
  public ResponseEntity allNoticeList(@PathVariable("facility_id") Long facilityId) {
      List<AllNoticeListDTO> allNoticeList;

      try {
        allNoticeList = allNoticeService.allNoticeList(facilityId);
      } catch (FacilityException e) {
        log.info("AllNoticeController 전체공지 목록: 해당 시설을 찾을 수 없습니다");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
      } catch (Exception e) {
        log.info("AllNoticeController 전체공지 목록: 찾는 과정에서 에러가 남");
          return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
      }

    // allNoticeId, create_date, title, content, important,  List<String> imageUrl
      return ResponseEntity.status(HttpStatus.OK).body(allNoticeList);
  }

  @PatchMapping("/v2/all-notices") // 전체공지 수정
  public ResponseEntity noticeEdit(@RequestPart(value="allnotice") AllNoticeEditDto dto,
                                   @RequestPart(value="file", required = false) List<MultipartFile> files) {

      try {
          allNoticeService.edit(dto, files);
      } catch (UserException | UserAuthException | AllNoticeException e) {
          log.info("AllNoticeController 전체공지 수정: 사용자 또는 공지사항 에러");
          return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
      } catch (Exception e) {
          log.info("AllNoticeController 전체공지 수정: 기타 에러");
          return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
      }

    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @DeleteMapping("/v2/all-notices") // 공지사항 삭제
  public ResponseEntity noticeDelete(@RequestBody Map<String, Long> allNotice) { //allnotice_id
      Long allNoticeId = allNotice.get("allnotice_id");

      if (allNoticeId == null)
          return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

      Long deletedCnt = allNoticeService.delete(allNoticeId);

      if (deletedCnt == 0)
          return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();

      return ResponseEntity.status(HttpStatus.OK).build();
  }

//    //  allNoticeId, user, facility, createDate, title, contents, important, images
//    @GetMapping("/v2/all-notices/detail/{allnotice_id}") // 전체공지 상세보기
//    public ResponseEntity allNoticeDetail(@PathVariable("allnotice_id") Long allNoticeId) {
//        AllNoticeResponse allNoticeResponse;
//
//        if (allNoticeId == null)
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//
//        try {
//          allNoticeResponse = allNoticeService.getDetail(allNoticeId);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//        }asdfafasdf
//
//        return ResponseEntity.status(HttpStatus.ACCEPTED).body(allNoticeResponse);
//        // allnotice_id, user_id, create_date, title, content, important, image_url;
//    }

}

