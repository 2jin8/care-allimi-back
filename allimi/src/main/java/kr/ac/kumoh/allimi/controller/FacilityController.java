package kr.ac.kumoh.allimi.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import kr.ac.kumoh.allimi.domain.Facility;
import kr.ac.kumoh.allimi.dto.facility.AddFacilityDTO;
import kr.ac.kumoh.allimi.dto.facility.EditFacilityDTO;
import kr.ac.kumoh.allimi.dto.facility.FacilityInfoDto;
import kr.ac.kumoh.allimi.exception.FacilityException;
import kr.ac.kumoh.allimi.exception.InputException;
import kr.ac.kumoh.allimi.service.FacilityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/v2")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class FacilityController {
  private final FacilityService facilityService;

  //시설 추가
  @PostMapping("/facilities")
  public ResponseEntity addFacility(@Valid @RequestBody AddFacilityDTO dto) { // name, address, tel, fm_name
    Long facilityId = facilityService.addFacility(dto);
    Map<String, Long> map = new HashMap<>();
    map.put("facility_id", facilityId);

    return ResponseEntity.status(HttpStatus.OK).body(map);
  }

  //시설 삭제
  @DeleteMapping("/facilities")
  public ResponseEntity deleteFacility(@RequestBody Map<String, Long> facility) throws Exception {
    Long facilityId = facility.get("facility_id");
    if (facilityId == null)
      throw new FacilityException("FacilityController 시설 삭제: facility_id가 null. 사용자의 잘못된 입력");

    facilityService.deleteFacility(facilityId);

    return ResponseEntity.status(HttpStatus.OK).build();
  }

  //시설 수정
  @PatchMapping("/facilities")
  public ResponseEntity modifyFacility(@NotNull @RequestBody EditFacilityDTO dto) throws Exception { // facility_id, name, address, tel, fm_name
    Long facilityId = facilityService.editFacility(dto);

    Map<String, Long> map = new HashMap<>();
    map.put("facility_id", facilityId);

    return ResponseEntity.status(HttpStatus.OK).body(map);
  }

  // 시설 정보 조회
  @GetMapping("/facilities/{facility_id}")
  public ResponseEntity facilityInfo(@PathVariable("facility_id") Long facilityId) throws Exception {
    if (facilityId == null)
      throw new InputException("FacilityController 시설 정보 조회: facility_id가 null로 들어옴. 사용자의 잘못된 요청");

    FacilityInfoDto dto = facilityService.getInfo(facilityId);

    return ResponseEntity.status(HttpStatus.OK).body(dto);
  }

  //시설 전체보기 - 관리자용
  @GetMapping("/facilities/admin")
  public ResponseEntity adminFacilityList(@PageableDefault(size = 20, sort = "name", direction = Sort.Direction.ASC) Pageable pageable) throws Exception {

    Page<Facility> facilities = facilityService.findAll(pageable);

    return ResponseEntity.status(HttpStatus.OK).body(facilities.getContent());
  }
}
