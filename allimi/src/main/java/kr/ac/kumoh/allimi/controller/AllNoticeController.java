package kr.ac.kumoh.allimi.controller;

import jakarta.validation.Valid;
import kr.ac.kumoh.allimi.domain.func.AllNotice;
import kr.ac.kumoh.allimi.dto.allNotice.AllNoticeEditDto;
import kr.ac.kumoh.allimi.dto.allNotice.AllNoticeListDTO;
import kr.ac.kumoh.allimi.dto.allNotice.AllNoticeWriteDto;
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
@RequestMapping("/v4")
@RestController
public class AllNoticeController {
    private final AllNoticeService allNoticeService;

    // 전체 공지사항 작성
    @PostMapping("/all-notices")
    public ResponseEntity allNoticeWrite(@Valid @RequestPart(value="allnotice") AllNoticeWriteDto dto,
                                         @RequestPart(value="file", required = false) List<MultipartFile> files) throws Exception {

        allNoticeService.write(dto, files);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // 전체공지 목록
    @GetMapping("/all-notices/{facility_id}")
    public ResponseEntity allNoticeList(@PathVariable("facility_id") Long facilityId) throws Exception {
        List<AllNoticeListDTO> allNoticeList= allNoticeService.allNoticeList(facilityId);

        return ResponseEntity.status(HttpStatus.OK).body(allNoticeList);
    }

    // 전체공지 수정
    @PatchMapping("/all-notices")
    public ResponseEntity noticeEdit(@Valid @RequestPart(value="allnotice") AllNoticeEditDto dto,
                                     @RequestPart(value="file", required = false) List<MultipartFile> files) throws Exception {

        allNoticeService.edit(dto, files);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // 공지사항 삭제
    @DeleteMapping("/all-notices")
    public ResponseEntity noticeDelete(@RequestBody Map<String, Long> allNotice) { //allnotice_id

        Long allNoticeId = allNotice.get("allnotice_id");
        if (allNoticeId == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        allNoticeService.delete(allNoticeId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}