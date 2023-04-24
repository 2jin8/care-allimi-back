package kr.ac.kumoh.allimi.controller;

import kr.ac.kumoh.allimi.dto.NHResidentDTO;
import kr.ac.kumoh.allimi.dto.ManagerNHResidentDTO;
import kr.ac.kumoh.allimi.service.NHResidentService;
import kr.ac.kumoh.allimi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
//시설장, 직원도 NHResident 추가가 필요함. role부여를 위해

@RestController
@RequiredArgsConstructor
public class NHResidentController {
    @Autowired
    private final UserService userService;
    private final NHResidentService nhResidentService;

    //새 입소자 추가
    @PostMapping("/v2/nhResident")
    public ResponseEntity addNHResident(@RequestBody NHResidentDTO dto) {
        try {
            userService.addNHResident(dto);
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    //직원, 시설장 역할부여
    @PostMapping("/v2/nhResident/manager")
    public ResponseEntity addRole(@RequestBody ManagerNHResidentDTO dto) {
        try {
            userService.addManagerNHResident(dto);
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

    //입소자 수정

}
