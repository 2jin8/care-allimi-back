package kr.ac.kumoh.allimi.controller;
import kr.ac.kumoh.allimi.dto.*;
import kr.ac.kumoh.allimi.exception.UserIdDuplicateException;
import kr.ac.kumoh.allimi.service.NHResidentService;
import lombok.RequiredArgsConstructor;
import kr.ac.kumoh.allimi.service.UserService;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final NHResidentService nhResidentService;

    //새 입소자 추가
    @PostMapping("/v1/nhResident")
    public ResponseEntity addNHResident(@RequestBody NHResidentDTO dto) {

        try {
            userService.addNHResident(dto);
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @PostMapping("/v1/users")  // 회원 가입
    public ResponseEntity addUser(@RequestBody SignUpDTO dto) {
        Long userId;

        try {
            userId = userService.addUser(dto);
        } catch(UserIdDuplicateException exception) { //중복된 이름
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch(Exception exception) { //그냥 에러
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseLogin(userId));
    }


    @PostMapping("/v1/login") // 로그인
    public ResponseEntity login(@RequestBody LoginDTO dto) {
        Long user_id;

        try {
            user_id = userService.login(dto.getId(), dto.getPassword());
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseLogin(user_id));
    }

    @Getter
    public class ResponseLogin {
        private Long user_id;

        public ResponseLogin(Long user_id) {
            this.user_id = user_id;
        }
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

    @GetMapping("/v1/users/{user_id}") // 사용자 정보 조회
    public ResponseEntity user_list(@PathVariable Long user_id) {
        if (user_id == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        UserListDTO userListDTO;

        try {
            userListDTO = userService.getUserInfo(user_id);
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(userListDTO);
    }

    @GetMapping("/v1/nhresdients/{facility_id}") // 입소자 모두 출력
    public ResponseEntity nhresidentList(@PathVariable("facility_id") Long facilityId) {
        List<NHResidentResponse> nhResidentResponses;
        try {
            nhResidentResponses = nhResidentService.getList(facilityId);
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(nhResidentResponses);
    }
}


