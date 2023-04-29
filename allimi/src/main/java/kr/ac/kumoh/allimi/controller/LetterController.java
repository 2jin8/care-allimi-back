package kr.ac.kumoh.allimi.controller;

import kr.ac.kumoh.allimi.domain.func.Letter;
import kr.ac.kumoh.allimi.dto.letter.LetterListDTO;
import kr.ac.kumoh.allimi.dto.letter.LetterWriteDto;
import kr.ac.kumoh.allimi.dto.notice.NoticeListDTO;
import kr.ac.kumoh.allimi.exception.FacilityException;
import kr.ac.kumoh.allimi.exception.NHResidentException;
import kr.ac.kumoh.allimi.exception.user.UserException;
import kr.ac.kumoh.allimi.service.LetterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
@Slf4j
@RestController
public class LetterController {
  private final LetterService letterService;

  // 한마디 작성
  @PostMapping(value = "/v2/letters")  // user_id, nhresident_id, facility_id, contents
  public ResponseEntity letterWrite(@RequestBody LetterWriteDto dto) {

    if (dto.getUser_id() == null || dto.getNhresident_id() == null || dto.getFacility_id() == null) {
      log.info("LetterController 한마디 작성: 필요한  값이 제대로 안들어옴. 사용자의 잘못된 입력");
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    Long letterId = null;

    try {
      letterId = letterService.write(dto);
    } catch (UserException | NHResidentException | FacilityException e) { //알림장 쓰기 실패
      log.info("LetterController 한마디 작성: user, resident, facility 중 하나 찾기 실패");
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    } catch (Exception e) { //알림장 쓰기 실패
      log.info("LetterController 한마디 작성: 한마디 쓰기 실패");
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    Map<String, Long> map = new HashMap<>();
    map.put("letter_id", letterId);

    return ResponseEntity.status(HttpStatus.OK).body(map);
  }

  @GetMapping("/v2/letters/{resident_id}") // 한마디 목록
  public ResponseEntity noticeList(@PathVariable("resident_id") Long residentId) {
    if (residentId == null) {
      log.info("LetterController 한마디 목록보기: 필요한 값이 제대로 안들어옴. 사용자의 잘못된 입력");
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    List<LetterListDTO> letterList;

    try {
      letterList = letterService.letterList(residentId);
    } catch (NHResidentException e) {
      log.info("LetterController 한마디 목록 확인: 해당하는 resident 찾기 실패");
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    return ResponseEntity.status(HttpStatus.OK).body(letterList);
  }
}
