package kr.ac.kumoh.allimi.controller;
import kr.ac.kumoh.allimi.domain.UserRole;
import kr.ac.kumoh.allimi.dto.*;
import kr.ac.kumoh.allimi.exception.UserIdDuplicateException;
import kr.ac.kumoh.allimi.service.NHResidentService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import kr.ac.kumoh.allimi.service.UserService;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {
    private final UserService userService;
    private final NHResidentService nhResidentService;

    //회원가입
    @PostMapping("/v2/users")
    public ResponseEntity addUser(@RequestBody SignUpDTO dto) {
        Long userId;

        try {
            userId = userService.addUser(dto);
        } catch(UserIdDuplicateException exception) { //중복된 id 에러
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch(Exception exception) { //그냥 에러
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        Map<String, Long> map = new HashMap<>();
        map.put("user_id", userId);

        return ResponseEntity.status(HttpStatus.OK).body(map);
    }

    @PostMapping("/v2/login") // 로그인
    public ResponseEntity login(@RequestBody LoginDTO dto) {
        ResponseLogin responseLogin;

        try {
            responseLogin = userService.login(dto.getId(), dto.getPassword());
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); //해당 id password 일치하는 게 없음
        }

        return ResponseEntity.status(HttpStatus.OK).body(responseLogin);
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

    @GetMapping("/v2/users/{user_id}") // 사용자 정보 조회
    public ResponseEntity userInfo(@PathVariable Long user_id) {
        if (user_id == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        UserListDTO userListDTO;

        try {
            userListDTO = userService.getUserInfo(user_id);
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); //user가 없는 경우
        }

        return ResponseEntity.status(HttpStatus.OK).body(userListDTO);
    }

    @DeleteMapping("/v1/users") // 회원 탈퇴
    public ResponseEntity deleteUser(@RequestBody Map<String, Long> user) {
        try {
            userService.deleteUser(user.get("user_id"));
        }catch (Exception exception) { //user를 찾을 수 없을 때
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/v1/logout") // 로그아웃
    public ResponseEntity logout(@RequestBody Map<String, Long> user) {
//        TODO: 로그아웃
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}


