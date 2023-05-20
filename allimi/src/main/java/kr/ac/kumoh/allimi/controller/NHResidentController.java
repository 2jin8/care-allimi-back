package kr.ac.kumoh.allimi.controller;

import jakarta.validation.Valid;
import kr.ac.kumoh.allimi.controller.response.NHResidentDetailResponse;
import kr.ac.kumoh.allimi.controller.response.ResponseResident;
import kr.ac.kumoh.allimi.dto.nhresident.NHResidentUFDTO;
import kr.ac.kumoh.allimi.dto.nhresident.NHResidentDTO;
import kr.ac.kumoh.allimi.dto.nhresident.NHResidentEditDTO;
import kr.ac.kumoh.allimi.dto.nhresident.NHResidentResponse;
import kr.ac.kumoh.allimi.exception.FacilityException;
import kr.ac.kumoh.allimi.exception.InputException;
import kr.ac.kumoh.allimi.exception.DataAlreadyExistsException;
import kr.ac.kumoh.allimi.exception.user.UserException;
import kr.ac.kumoh.allimi.service.NHResidentService;
import kr.ac.kumoh.allimi.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//시설장, 직원도 NHResident 추가가 필요함. role부여를 위해
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/v4")
@Slf4j
@RestController
public class NHResidentController {
  private final UserService userService;
  private final NHResidentService nhResidentService;

  //새 입소자 추가 or 직원, 시설장 등록
  @PostMapping("/nhResidents")
  public ResponseEntity addNHResident(@Valid @RequestBody NHResidentDTO dto) throws Exception { // user_id, facility_id, resident_name, birth, user_role, health_info;
    boolean hasData = nhResidentService.findByFacilityAndUserAndUserRoleExists(dto.getFacility_id(), dto.getUser_id(), dto.getUser_role());
    //입소자 중복 추가 방지용 - 해당시설에 해당 user가 해당 역할로 등록되어있는지 확인
    if (hasData) {
      log.info("NHResident 추가: 중복된 요청. 이미 있는 입소자임");
      throw new DataAlreadyExistsException("이미 존재하는 입소자");
    }

    Long residentId = nhResidentService.addNHResident(dto);

    Map<String, Long> map = new HashMap<>();
    map.put("resident_id", residentId);

    return ResponseEntity.ok().body(map);
  }

  //입소자 삭제
  @DeleteMapping("/nhResidents")
  public ResponseEntity deleteResident(@RequestBody Map<String, Long> resident) throws Exception { //nhresident_id
    Long residentId = resident.get("nhresident_id");
    if (residentId == null)
      throw new InputException("NHResidentController 입소자 삭제: user_id 또는 nhresident_id가 null. 잘못된 입력");

    nhResidentService.deleteResident(residentId);

    return ResponseEntity.status(HttpStatus.OK).build();
  }

  //입소자 수정
  @PatchMapping("/nhResidents") //resident_id
  public ResponseEntity nhresidentEdit(@Valid @RequestBody NHResidentEditDTO editDTO) throws Exception { //resident_id, resident_name, birth, health_info
    nhResidentService.editNHResident(editDTO);

    Map<String, Long> map = new HashMap<>();
    map.put("resident_id", editDTO.getResident_id());

    return ResponseEntity.status(HttpStatus.OK).body(map);
  }

  @Getter
  @AllArgsConstructor
  public class ResponseResidentList {
    private int count;
    private List<NHResidentResponse> resident_list;
  }

  // 사용자의 입소자 리스트 출력
  @GetMapping("/nhResidents/users/{user_id}")
  public ResponseEntity usersNHResidentList(@PathVariable("user_id") Long userId) throws Exception { //user_id
    if (userId == null)
      throw new UserException("NHResidentController 입소자 리스트 출력: user_id가 null. 사용자의 잘못된 입력");

    List<NHResidentResponse> nhResidentResponses = userService.getNHResidents(userId);

    // count, resident_list: {resident_id, acility_id, facility_name, resident_name, user_role};
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseResidentList(nhResidentResponses.size(), nhResidentResponses));
  }

  // 입소자의 상세정보 출력
  @GetMapping("/nhResidents/{nhr_id}")
  public ResponseEntity nhresidentList(@PathVariable("nhr_id") Long nhrId) throws Exception { //nhr_id
    if (nhrId == null)
      throw new InputException("NHResidentController 입소자 상세정보 출력: nhr_id가 null. 잘못된 입력");

    NHResidentDetailResponse response = nhResidentService.getNHResidentInfo(nhrId);

    return ResponseEntity.status(HttpStatus.OK).body(response);
    // count, resident_list: {resident_id, acility_id, facility_name, resident_name, user_role};
  }

  // 시설에 포함된 모든 protector 출력
  @GetMapping("/nhResidents/protectors/{facility_id}")
  public ResponseEntity protectorList(@PathVariable("facility_id") Long facilityId) throws Exception {
    List<ResponseResident> facilities = nhResidentService.findProtectorByFacility(facilityId);

    return ResponseEntity.status(HttpStatus.OK).body(facilities);
  }

  // 시설에 포함된 모든 직원 출력
  @GetMapping("/nhResidents/workers/{facility_id}")
  public ResponseEntity workerList(@PathVariable("facility_id") Long facilityId) throws Exception {
    List<ResponseResident> facilitys = nhResidentService.findWorkerByFacility(facilityId);

    return ResponseEntity.status(HttpStatus.OK).body(facilitys);
  }

  // 시설 포함된 모든 입소자 출력 - 관리자용
  @GetMapping("/nhResidents/facility/{facility_id}")
  public ResponseEntity allResidentList(@PathVariable("facility_id") Long facilityId) throws Exception {
    if (facilityId == null)
      throw new FacilityException("NHResidentController 시설의 모든 입소자 출력 - 관리자용: facility_id가 null. 사용자의 잘못된 입력");

    List<ResponseResident> facilitys = nhResidentService.findAllByFacility(facilityId);

    return ResponseEntity.status(HttpStatus.OK).body(facilitys);
  }


  // worker_id 설정 (요양보호사가 관리하는 입소자 등록)
  @PostMapping("/nhResidents/manage")
  public ResponseEntity setWorker(@Valid @RequestBody NHResidentUFDTO setDTO) throws Exception {  // nhresident_id, user_id, facility_id
    nhResidentService.setWorker(setDTO);

    return ResponseEntity.status(HttpStatus.OK).build();
  }

  // 요양보호사가 관리하는 입소자 목록
  @GetMapping("/nhResidents/manage")
  public ResponseEntity manageNHResidentList(@RequestBody Map<String, Long> resident) throws Exception {
    Long user_id = resident.get("user_id");
    if (user_id == null)
      throw new InputException("NHResidentController 요양보호사가 관리하는 입소자 목록: user_id or facility_id가 null로 들어옴");

    List<NHResidentResponse> responseList = nhResidentService.workerList(user_id);

    return ResponseEntity.status(HttpStatus.OK).body(responseList);
  }


//  // 사용자가 등록한 요양원 목록 - 어디에 쓰는건지 잘 모르겠어서 일단 주석: 사용자의 입소자 목록을 출력하면 되지않나? "/nhResidents/users/{user_id}"
//  @GetMapping("/nhResidents/facility/{user_id}/list")
//  public ResponseEntity nhResidentWithFacilityList(@PathVariable Long user_id) throws Exception {
//    if (user_id == null)
//      throw new InputException("NHResidentController 사용자가 등록한 요양원 목록: user_id가 null로 들어옴");
//
//    List<NHResidentResponse> nhResidentResponses = userService.getNHResidentsWithFacility(user_id);
//
//    return ResponseEntity.status(HttpStatus.OK).body(new ResponseResidentList(nhResidentResponses.size(), nhResidentResponses));
//  }

}

