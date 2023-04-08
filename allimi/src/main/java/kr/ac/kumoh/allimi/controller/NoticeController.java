package kr.ac.kumoh.allimi.controller;

import kr.ac.kumoh.allimi.domain.Notice;
import kr.ac.kumoh.allimi.dto.*;
import kr.ac.kumoh.allimi.domain.UserRole;
import kr.ac.kumoh.allimi.service.NoticeService;
import kr.ac.kumoh.allimi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private UserService userService;


    @GetMapping("/v1/notices/{user_id}") // 알림장 목록
    public ResponseEntity noticeList(@PathVariable Long user_id) {

        UserRole userRole = userService.getUserRole(user_id);

        List<NoticeListDTO> noticeList;

        if (userRole == UserRole.PROTECTOR) { // 보호자
            noticeList = noticeService.userNoticeList(user_id);
        } else { // 직원 or 시설장
            noticeList = noticeService.noticeList(user_id);
        }

        return ResponseEntity.status(HttpStatus.OK).body(noticeList);
    }

    @GetMapping("/v1/notices/detail/{notice_id}") // 알림장 상세보기
    public ResponseEntity notice(@PathVariable("notice_id") Long noticeId) {

        NoticeResponse noticeResponse = noticeService.findNotice(noticeId);

        if (noticeResponse == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(noticeResponse);
    }

    @PostMapping("/v1/notices") // 알림장 작성
    public ResponseEntity noticeWrite(@RequestBody NoticeWriteDto dto) {
        Notice writeNotice = noticeService.write(dto);
        if (writeNotice == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping("/v1/notices") // 알림장 수정
    public ResponseEntity noticeEdit(@RequestBody NoticeEditDto dto) {
        noticeService.edit(dto);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/v1/notices") // 알림장 삭제
    public ResponseEntity noticeDelete(@RequestBody IdDTO dto) {

        Long deletedCnt = noticeService.delete(dto.getId());

        if (deletedCnt == 0)
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}

