package kr.ac.kumoh.allimi.controller;

import kr.ac.kumoh.allimi.controller.response.ResponseResident;
import kr.ac.kumoh.allimi.domain.UserRole;
import kr.ac.kumoh.allimi.dto.nhresident.NHResidentDTO;
import kr.ac.kumoh.allimi.dto.nhresident.NHResidentEditDTO;
import kr.ac.kumoh.allimi.dto.nhresident.NHResidentResponse;
import kr.ac.kumoh.allimi.exception.FacilityException;
import kr.ac.kumoh.allimi.exception.NHResidentException;
import kr.ac.kumoh.allimi.exception.user.UserAuthException;
import kr.ac.kumoh.allimi.exception.user.UserException;
import kr.ac.kumoh.allimi.service.FacilityService;
import kr.ac.kumoh.allimi.service.InvitationService;
import kr.ac.kumoh.allimi.service.NHResidentService;
import kr.ac.kumoh.allimi.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
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
    private final InvitationService invitationService;

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

    //입소자 삭제 //잘못된 정보 입력 테스트 - 개선이 필요할듯
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
        if (nhResidentResponses == null) {
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

    // 사용자의 입소자 리스트 출력
    @GetMapping("/v2/nhResidents/{user_id}")
    public ResponseEntity nhresidentList(@PathVariable("user_id") Long userId) { //user_id

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

    // 시설 해당하는 모든 입소자 출력
    @GetMapping("/v2/nhResidents/facility/{facility_id}")
    public ResponseEntity allResidentList(@PathVariable("facility_id") Long facilityId) {
        if (facilityId == null) {
          log.info("NHResidentController 모든 입소자 출력 - 관리자용: nhresident_id값이 제대로 안들어옴. 사용자의 잘못된 입력");
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
}
