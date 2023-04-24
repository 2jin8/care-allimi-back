package kr.ac.kumoh.allimi.controller;

import kr.ac.kumoh.allimi.dto.nhresident.NHResidentDTO;
import kr.ac.kumoh.allimi.dto.nhresident.NHResidentResponse;
import kr.ac.kumoh.allimi.service.NHResidentService;
import kr.ac.kumoh.allimi.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
//시설장, 직원도 NHResident 추가가 필요함. role부여를 위해

@RestController
@RequiredArgsConstructor
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


    // 사용자의 입소자 리스트 출력
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

    //approved = false인 사용자 모두 출력
    @GetMapping("/v2/nhresdients/approved")
    public void approvedFalseList() {
        //TODO
    }

    //승인해주기
    @PostMapping("/v2/nhresidents/approved/{resident_id}")
    public void approve() {
        //TODO
    }
}
