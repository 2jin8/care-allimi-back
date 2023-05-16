package kr.ac.kumoh.allimi.controller;

import jakarta.validation.Valid;
import kr.ac.kumoh.allimi.dto.allNotice.AllNoticeEditDto;
import kr.ac.kumoh.allimi.dto.allNotice.AllNoticeListDTO;
import kr.ac.kumoh.allimi.dto.allNotice.AllNoticeWriteDto;
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
    public ResponseEntity allNoticeWrite(@Valid @RequestPart(value="allnotice") AllNoticeWriteDto dto,
                                    @RequestPart(value="file", required = false) List<MultipartFile> files) throws Exception {

        allNoticeService.write(dto, files);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // 전체공지 목록
    @GetMapping("/v2/all-notices/{facility_id}")
    public ResponseEntity allNoticeList(@PathVariable("facility_id") Long facilityId) throws Exception {
        List<AllNoticeListDTO> allNoticeList= allNoticeService.allNoticeList(facilityId);

        // allNoticeId, create_date, title, content, important,  List<String> imageUrl
        return ResponseEntity.status(HttpStatus.OK).body(allNoticeList);
  }

    // 전체공지 수정
    @PatchMapping("/v2/all-notices")
    public ResponseEntity noticeEdit(@Valid @RequestPart(value="allnotice") AllNoticeEditDto dto,
                                   @RequestPart(value="file", required = false) List<MultipartFile> files) throws Exception {

        allNoticeService.edit(dto, files);

        return ResponseEntity.status(HttpStatus.OK).build();
  }

    // 공지사항 삭제
    @DeleteMapping("/v2/all-notices")
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

