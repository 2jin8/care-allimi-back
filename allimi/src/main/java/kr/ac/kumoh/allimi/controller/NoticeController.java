package kr.ac.kumoh.allimi.controller;

import kr.ac.kumoh.allimi.domain.Notice;
import kr.ac.kumoh.allimi.dto.*;
import kr.ac.kumoh.allimi.service.NoticeService;
import kr.ac.kumoh.allimi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class NoticeController {
  private final NoticeService noticeService;
  private final UserService userService;

  @PostMapping("/v2/notices") // 알림장 작성
  public ResponseEntity noticeWrite(@RequestBody NoticeWriteDto dto) {
//    Long noticeId;
    try {
      noticeService.write(dto);
    } catch (Exception e) { //알림장 쓰기 실패
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @GetMapping("/v2/notices/{resident_id}") // 알림장 목록
  public ResponseEntity noticeList(@PathVariable("resident_id") Long residentId) {
    List<NoticeListDTO> noticeList;

    try {
      noticeList = noticeService.noticeList(residentId);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    return ResponseEntity.status(HttpStatus.OK).body(noticeList);
  }

  @GetMapping("/v2/notices/detail/{notice_id}") // 알림장 상세보기
  public ResponseEntity noticeDetail(@PathVariable("notice_id") Long noticeId) {
    NoticeResponse noticeResponse;

    try {
      noticeResponse = noticeService.getDetail(noticeId);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    return ResponseEntity.status(HttpStatus.ACCEPTED).body(noticeResponse);
  }

//  @PatchMapping("/v1/notices") // 알림장 수정
//  public ResponseEntity noticeEdit(@RequestBody NoticeEditDto dto) {
//    try {
//      noticeService.edit(dto);
//    } catch (Exception e) {
//      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//    }
//
//    return ResponseEntity.status(HttpStatus.OK).build();
//  }
//
//  @DeleteMapping("/v1/notices") // 알림장 삭제
//  public ResponseEntity noticeDelete(@RequestBody IdDTO dto) {
//    Long deletedCnt = noticeService.delete(dto.getId());
//
//    if (deletedCnt == 0)
//      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
//
//    return ResponseEntity.status(HttpStatus.OK).build();
//  }
}

