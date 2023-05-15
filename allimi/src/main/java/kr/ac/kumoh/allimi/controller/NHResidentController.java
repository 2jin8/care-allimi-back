package kr.ac.kumoh.allimi.controller;

import kr.ac.kumoh.allimi.controller.response.NHResidentDetailResponse;
import kr.ac.kumoh.allimi.controller.response.ResponseResident;
import kr.ac.kumoh.allimi.dto.nhresident.NHResidentUFDTO;
import kr.ac.kumoh.allimi.dto.nhresident.NHResidentDTO;
import kr.ac.kumoh.allimi.dto.nhresident.NHResidentEditDTO;
import kr.ac.kumoh.allimi.dto.nhresident.NHResidentResponse;
import kr.ac.kumoh.allimi.exception.FacilityException;
import kr.ac.kumoh.allimi.exception.NHResidentException;
import kr.ac.kumoh.allimi.exception.user.UserException;
import kr.ac.kumoh.allimi.service.NHResidentService;
import kr.ac.kumoh.allimi.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


//시설장, 직원도 NHResident 추가가 필요함. role부여를 위해
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
@RestController
public class NHResidentController {
    @Autowired
    private final UserService userService;
    private final NHResidentService nhResidentService;


    //새 입소자 추가 or 직원, 시설장 등록
    @PostMapping("/v2/nhResidents")
    public ResponseEntity addNHResident(@RequestBody NHResidentDTO dto) { // user_id, facility_id, resident_name, birth, user_role, health_info;

      if (dto.getUser_id() == null || dto.getFacility_id() == null) {
        log.info("NHResident 추가: user_id가 null로 들어옴. 잘못된 요청");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
      }

//      for (UserRole userRole : UserRole.values()) {
//        if (!userRole.name().equals(dto.getUser_role())) {
//          log.info("NHResident 추가: userRole이 올바른 값이 안들어옴. 잘못된 요청");
//          return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//        }
//      }

      //입소자 중복 추가 방지용
      boolean hasData = nhResidentService.findByFacilityAndUserExists(dto.getFacility_id(), dto.getUser_id());

      if (hasData) {
        log.info("NHResident 추가: 중복된 요청. 이미 있는 입소자임");
        return ResponseEntity.status(HttpStatus.CONFLICT).build(); //409
      }

      Long residentId;

      try {
        residentId = nhResidentService.addNHResident(dto);
      } catch (UserException exception) {
        log.info("NHResident 추가: user_id해당하는 user가 없음");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
      } catch (FacilityException exception) {
        log.info("NHResident 추가: facility_id해당하는 시설이 없음");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
      } catch (Exception exception) {
        log.info("NHResident 추가: 그냥 오류남");
          return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
      }

      Map<String, Long> map = new HashMap<>();
      map.put("resident_id", residentId);

      return ResponseEntity.status(HttpStatus.OK).body(map);
    }

    //입소자 삭제
    @DeleteMapping("/v2/nhResidents")
    public ResponseEntity deleteResident(@RequestBody Map<String, Long> resident) { //nhresident_id
      Long userId = resident.get("user_id");
      Long residentId = resident.get("nhresident_id");

      if (residentId == null || userId == null) {
        log.info("NHResidentController 입소자 삭제: 값이 제대로 안들어옴. 사용자의 잘못된 입력");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
      }

      List<NHResidentResponse> nhResidentResponses;

      try {
        nhResidentService.deleteResident(residentId);
        nhResidentResponses = userService.getNHResidents(userId);
        if (nhResidentResponses.size() == 0) {
          userService.setNHResidentNull(userId);
        } else {
          userService.changeNHResident(userId, nhResidentResponses.get(0).getResident_id());
        }
      } catch (Exception exception) { //nhresident를 찾을 수 없을 때
        log.info("NHResidentController 입소자 삭제: 삭제 중 문제발생");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
      }

      return ResponseEntity.status(HttpStatus.OK).build();
    }

  // 사용자의 입소자 리스트 출력 - v2
  @GetMapping("/v2/nhResidents/{user_id}")
  public ResponseEntity v2UsersNHResidentList(@PathVariable("user_id") Long userId) { //user_id

    if (userId == null)
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

    List<NHResidentResponse> nhResidentResponses;

    try {
      nhResidentResponses = userService.getNHResidents(userId);
    } catch (UserException exception) {
      log.info("NHResidentController 사용자 입소자 리스트 출력: user를 찾을 수 없음");
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    } catch (Exception exception) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    return ResponseEntity.status(HttpStatus.OK).body(new ResponseResidentList(nhResidentResponses.size(), nhResidentResponses));
    // count, resident_list: {resident_id, acility_id, facility_name, resident_name, user_role};
  }

    // 사용자의 입소자 리스트 출력
    @GetMapping("/v3/nhResidents/users/{user_id}")
    public ResponseEntity usersNHResidentList(@PathVariable("user_id") Long userId) { //user_id

      if (userId == null)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

      List<NHResidentResponse> nhResidentResponses;

      try {
          nhResidentResponses = userService.getNHResidents(userId);
      } catch (UserException exception) {
        log.info("NHResidentController 사용자 입소자 리스트 출력: user를 찾을 수 없음");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
      } catch (Exception exception) {
          return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
      }

      return ResponseEntity.status(HttpStatus.OK).body(new ResponseResidentList(nhResidentResponses.size(), nhResidentResponses));
      // count, resident_list: {resident_id, acility_id, facility_name, resident_name, user_role};
    }

  // 입소자의 상세정보 출력
  @GetMapping("/v3/nhResidents/{nhr_id}")
  public ResponseEntity nhresidentList(@PathVariable("nhr_id") Long nhrId) { //nhr_id

    if (nhrId == null)
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

    NHResidentDetailResponse response;

    try {
      response = nhResidentService.getNHResidentInfo(nhrId);
    } catch (NHResidentException exception) {
      log.info("NHResidentController 사용자 입소자 리스트 출력: nhResident를 찾을 수 없음");
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    } catch (Exception exception) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    return ResponseEntity.status(HttpStatus.OK).body(response);
    // count, resident_list: {resident_id, acility_id, facility_name, resident_name, user_role};
  }


  @Getter
  @AllArgsConstructor
  public class ResponseResidentList {
      private int count;
      private List<NHResidentResponse> resident_list;
  }


  // 시설에 포함된 모든 protector 출력
  @GetMapping("/v2/nhResidents/protectors/{facility_id}")
  public ResponseEntity protectorList(@PathVariable("facility_id") Long facilityId) {
    if (facilityId == null) {
      log.info("NHResidentController 모든 보호자 출력: nhresident_id값이 제대로 안들어옴. 사용자의 잘못된 입력");
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    List<ResponseResident> facilitys;

    try {
      facilitys = nhResidentService.findProtectorByFacility(facilityId);
    }catch (Exception exception) {
      log.info("NHResidentController 모든 보호자 출력: nhresident 리스트 찾기 실패");
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    return ResponseEntity.status(HttpStatus.OK).body(facilitys);
  }

  // 시설 포함된 모든 입소자 출력
  @GetMapping("/v2/nhResidents/facility/{facility_id}")
  public ResponseEntity allResidentList(@PathVariable("facility_id") Long facilityId) {
      if (facilityId == null) {
        log.info("NHResidentController 모든 입소자 출력 - 관리자용: facility_id 값이 제대로 안들어옴. 사용자의 잘못된 입력");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
      }

    List<ResponseResident> facilitys;

      try {
        facilitys = nhResidentService.findAllByFacility(facilityId);
      }catch (Exception exception) {
        log.info("NHResidentController 모든 입소자 출력 - 관리자용: nhresident 리스트 찾기 실패");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
      }

    return ResponseEntity.status(HttpStatus.OK).body(facilitys);
  }

  //입소자 수정
  @PatchMapping("/v2/nhResidents") //resident_id
  public ResponseEntity nhresidentEdit(@RequestBody NHResidentEditDTO editDTO) { //resident_id, resident_name, birth, health_info

    if (editDTO.getResident_id() == null) {
      log.info("NHResidentController 입소자 수정: nhresident_id값이 제대로 안들어옴. 사용자의 잘못된 입력");
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    try {
      nhResidentService.editNHResident(editDTO);
    } catch (NHResidentException exception) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    } catch (Exception exception) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    // resident_id, resident_name, birth, health_info;
    Map<String, Long> map = new HashMap<>();
    map.put("resident_id", editDTO.getResident_id());

    return ResponseEntity.status(HttpStatus.OK).body(map);
  }

  // currentResident 수정
  @PostMapping("/v2/nhResidents/change")
  public ResponseEntity residentChange(@RequestBody NHResidentUFDTO changeDTO) {

      NHResidentResponse nhResidentResponse;

      try {
          nhResidentResponse = nhResidentService.changeCurrentResident(changeDTO);
      } catch (Exception e) {
          log.info("NHResidentController 입소자 변경: 사용자 or 시설 or 입소자 찾을 수 없음");
          return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
      }

      return ResponseEntity.status(HttpStatus.OK).body(nhResidentResponse);
  }

  // 사용자가 등록한 요양원 목록
  @GetMapping("/v2/nhResidents/facility/{user_id}/list")
  public ResponseEntity nhResidentWithFacilityList(@PathVariable Long user_id) throws Exception {
      if (user_id == null) {
          log.info("NHResidentController 사용자가 등록한 요양원 목록: user_id가 null로 들어옴");
          return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
      }

      List<NHResidentResponse> nhResidentResponses = userService.getNHResidentsWithFacility(user_id);

      return ResponseEntity.status(HttpStatus.OK).body(new ResponseResidentList(nhResidentResponses.size(), nhResidentResponses));
  }

  // worker_id 설정
  @PostMapping("/v2/nhResidents/manage")
  public ResponseEntity setWorker(@RequestBody NHResidentUFDTO setDTO) throws Exception {

      if (setDTO.getFacility_id() == null || setDTO.getNhresident_id() == null || setDTO.getUser_id() == null) {
          log.info("NHResidentController 요양보호사 추가: facility_id or nhresident_id or user_id 값이 null로 들어옴");
          return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
      }

      nhResidentService.setWorker(setDTO);

      return ResponseEntity.status(HttpStatus.OK).build();
  }

  // 요양보호사가 관리하는 입소자 목록
  @PostMapping("/v2/nhResidents/manage/list")
  public ResponseEntity manageNHResidentList(@RequestBody Map<String, Long> resident) throws Exception {
      Long user_id = resident.get("user_id");
      Long facility_id = resident.get("facility_id");

      if (user_id == null || facility_id == null) {
          log.info("NHResidentController 요양보호사가 관리하는 입소자 목록: user_id or facility_id가 null로 들어옴");
          return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
      }

      List<NHResidentResponse> responseList = nhResidentService.workerList(user_id, facility_id);

      return ResponseEntity.status(HttpStatus.OK).body(responseList);
  }
}
