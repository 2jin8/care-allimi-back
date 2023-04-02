package kr.ac.kumoh.allimi.controller;

import kr.ac.kumoh.allimi.domain.Notice;
import kr.ac.kumoh.allimi.dto.NoticeResponse;
import kr.ac.kumoh.allimi.dto.NoticeWriteDto;
import kr.ac.kumoh.allimi.service.NoticeService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    @GetMapping("/v1/notices")
    public ResponseEntity noticeList() {
        List<NoticeResponse> noticeResponses = noticeService.noticeList();

        for (NoticeResponse nr : noticeResponses) {
            System.out.println(nr.getContent());
        }

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(noticeResponses.toArray());
    }

//    @GetMapping("/v1/notices/{user_id}")
//    public ResponseEntity noticeList(@PathVariable Long user_id) {
//
//        List<Notice> notices = noticeService.noticeList();
//
//        return ResponseEntity.status(HttpStatus.ACCEPTED).body(notices);
//    }

    @PostMapping("/v1/notices")
    public ResponseEntity noticeWrite(@RequestBody NoticeWriteDto dto) {

        Notice writeNotice = noticeService.write(dto);
        if (writeNotice == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}


