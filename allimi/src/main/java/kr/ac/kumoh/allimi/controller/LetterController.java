package kr.ac.kumoh.allimi.controller;

import kr.ac.kumoh.allimi.domain.func.Letter;
import kr.ac.kumoh.allimi.dto.letter.LetterEditDto;
import kr.ac.kumoh.allimi.dto.letter.LetterListDTO;
import kr.ac.kumoh.allimi.dto.letter.LetterWriteDto;
import kr.ac.kumoh.allimi.dto.notice.NoticeListDTO;
import kr.ac.kumoh.allimi.exception.FacilityException;
import kr.ac.kumoh.allimi.exception.LetterException;
import kr.ac.kumoh.allimi.exception.NHResidentException;
import kr.ac.kumoh.allimi.exception.user.UserAuthException;
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
  
  // 읽음 표시
  @PostMapping("/v2/letters/read")
  public ResponseEntity readCheck(@RequestBody Map<String, Long> info) { // user_id, letter_id

    Long userId = info.get("user_id");
    Long letterId = info.get("letter_id");

    if (userId == null || letterId == null) {
      log.info("LetterController 한마디 읽기: 필요한  값이 제대로 안들어옴. 사용자의 잘못된 입력");
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    try {
      letterService.readCheck(userId, letterId);
    } catch (UserAuthException e) {
      log.info("LetterController 한마디 읽기: 한마디를 읽을 권한이 없음");
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    } catch (UserException | LetterException e) {
      log.info("LetterController 한마디 읽기: user, letter 중 하나 찾기 실패");
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    } catch (Exception e) {
      log.info("LetterController 한마디 읽기: 한마디 읽기 실패");
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    Map<String, Long> map = new HashMap<>();
    map.put("letter_id", letterId);

    return ResponseEntity.status(HttpStatus.OK).body(map);
  }

  // 한마디 수정
  @PatchMapping(value = "/v2/letters")
  public ResponseEntity letterEdit(@RequestBody LetterEditDto dto) {  // letter_id, user_id, nhresident_id, contents
    if (dto.getLetter_id() == null || dto.getNhresident_id() == null || dto.getUser_id() == null) {
      log.info("LetterController 한마디 수정: 필요한  값이 제대로 안들어옴. 사용자의 잘못된 입력");
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    try {
      letterService.edit(dto);
    } catch (UserAuthException e) { //알림장 쓰기 실패
      log.info("LetterController 한마디 수정: user, resident, facility 중 하나 찾기 실패");
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    } catch (UserException | NHResidentException | LetterException e) { //알림장 쓰기 실패
      log.info("LetterController 한마디 수정: user, resident, letter 중 하나 찾기 실패");
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    } catch (Exception e) { //알림장 쓰기 실패
      log.info("LetterController 한마디 수정: 한마디 쓰기 실패");
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    Map<String, Long> map = new HashMap<>();
    map.put("letter_id", dto.getLetter_id());

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
