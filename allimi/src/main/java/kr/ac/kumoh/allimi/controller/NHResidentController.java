package kr.ac.kumoh.allimi.controller;

import kr.ac.kumoh.allimi.dto.nhresident.NHResidentDTO;
import kr.ac.kumoh.allimi.dto.nhresident.NHResidentResponse;
import kr.ac.kumoh.allimi.exception.NHResidentException;
import kr.ac.kumoh.allimi.exception.user.UserAuthException;
import kr.ac.kumoh.allimi.exception.user.UserException;
import kr.ac.kumoh.allimi.service.NHResidentService;
import kr.ac.kumoh.allimi.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//시설장, 직원도 NHResident 추가가 필요함. role부여를 위해

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class NHResidentController {
    @Autowired
    private final UserService userService;
    private final NHResidentService nhResidentService;

    //새 입소자 추가 or 직원, 시설장 등록
    @PostMapping("/v2/nhResident")
    public ResponseEntity addNHResident(@RequestBody NHResidentDTO dto) {
        try {
            userService.addNHResident(dto);
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    //입소자 삭제
    @DeleteMapping("/v2/nhResident")
    public ResponseEntity deleteResident(@RequestBody Map<String, Long> resident) {
        try {
            nhResidentService.deleteResident(resident.get("nhresident_id"));
        } catch (Exception exception) { //nhresident를 찾을 수 없을 때
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // 사용자의 (승인된) 입소자 리스트 출력
    @GetMapping("/v2/nhresdients/{user_id}")
    public ResponseEntity nhresidentList(@PathVariable("user_id") Long userId) {
        List<NHResidentResponse> nhResidentResponses;

        try {
            nhResidentResponses = userService.getNHResidents(userId);
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseResidentList(nhResidentResponses.size(), nhResidentResponses));
    }

    @Getter
    @AllArgsConstructor
    public class ResponseResidentList {
        private int count;
        private List<NHResidentResponse> userListDTO;
    }

    //입소자 수정
    @PatchMapping("/v2/nhresidents/{resident_id}")
    public void nhresidentEdit(@PathVariable("resident_id") Long residentId) {
        //TODO
    }

    //is_approved = false인 사용자 모두 출력
    @GetMapping("/v2/nhresdients/approval/{facility_id}")
    public ResponseEntity approvedFalseList(@PathVariable("facility_id") Long facilityId) {
        List<NHResidentResponse> responses = new ArrayList<>();

        try {
            responses = nhResidentService.notApprovedList(facilityId);
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    //승인해주기
    @PostMapping("/v2/nhresidents/approval")
    public ResponseEntity approve(@RequestBody Map<String, Long> data) {
        Long approverId = data.get("approver_id"); //승인해주는 사람 resident_id
        Long residentId = data.get("resident_id"); //승인받을 입소자 resident_id

        if (approverId == null || residentId == null)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

        try {
            nhResidentService.approve(approverId, residentId);
        } catch(NHResidentException exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); //사용자를 찾을 수 없음
        } catch(UserAuthException exception) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); //권한이 없는 사용자
        } catch(Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        Map<String, Long> map = new HashMap<>();
        map.put("resident_id", residentId);

        return ResponseEntity.status(HttpStatus.OK).body(map);
    }
}
