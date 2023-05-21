package kr.ac.kumoh.allimi.controller;

import jakarta.validation.Valid;
import kr.ac.kumoh.allimi.dto.letter.LetterEditDto;
import kr.ac.kumoh.allimi.dto.letter.LetterListDTO;
import kr.ac.kumoh.allimi.dto.letter.LetterWriteDto;
import kr.ac.kumoh.allimi.exception.InputException;
import kr.ac.kumoh.allimi.exception.NHResidentException;
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
@RequestMapping("/v4")
@Slf4j
@RestController
public class LetterController {
  private final LetterService letterService;

  // 한마디 작성
  @PostMapping(value = "/letters")  // resident_id(글 쓰는 사람), contents
  public ResponseEntity letterWrite(@Valid @RequestBody LetterWriteDto dto) throws Exception {
    Long letterId = letterService.write(dto);

    Map<String, Long> map = new HashMap<>();
    map.put("letter_id", letterId);

    return ResponseEntity.status(HttpStatus.OK).body(map);
  }

  // 읽음 표시 - 안씀
  @PostMapping("/letters/read")
  public ResponseEntity readCheck(@RequestBody Map<String, Long> info) throws Exception { // resident_id, letter_id
    Long residentId = info.get("resident_id");
    Long letterId = info.get("letter_id");
    if (residentId == null || letterId == null)
      throw new InputException("LetterController 한마디 읽기: user_id 또는 letter_id가 null. 사용자의 잘못된 입력");

    letterService.readCheck(residentId, letterId);

    Map<String, Long> map = new HashMap<>();
    map.put("letter_id", letterId);

    return ResponseEntity.status(HttpStatus.OK).body(map);
  }

  // 한마디 수정
  @PatchMapping(value = "/letters") // letter_id, writer_id(nhr), contents
  public ResponseEntity letterEdit(@Valid @RequestBody LetterEditDto dto) throws Exception {
    letterService.edit(dto);

    Map<String, Long> map = new HashMap<>();
    map.put("letter_id", dto.getLetter_id());

    return ResponseEntity.status(HttpStatus.OK).body(map);
  }

  @DeleteMapping("/letters") // 한마디 삭제
  public ResponseEntity letterDelete(@RequestBody Map<String, Long> letter) {
    Long letterId = letter.get("letter_id");
    if (letterId == null)
      throw new InputException("LetterController 한마디 삭제: letter_id가 null. 사용자의 잘못된 입력");

    Long deletedCnt = letterService.delete(letterId);
    if (deletedCnt == 0)
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @GetMapping("/letters/{resident_id}") // 한마디 목록
  public ResponseEntity noticeList(@PathVariable("resident_id") Long residentId) throws Exception {
    if (residentId == null)
      throw new InputException("LetterController 한마디 목록보기: resident_id가 null. 사용자의 잘못된 입력");

    List<LetterListDTO> letterList = letterService.letterList(residentId);

    return ResponseEntity.status(HttpStatus.OK).body(letterList);
  }
}
