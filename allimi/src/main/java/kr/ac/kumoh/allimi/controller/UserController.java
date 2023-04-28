package kr.ac.kumoh.allimi.controller;
import kr.ac.kumoh.allimi.controller.response.ResponseLogin;
import kr.ac.kumoh.allimi.domain.UserRole;
import kr.ac.kumoh.allimi.dto.user.LoginDTO;
import kr.ac.kumoh.allimi.dto.user.SignUpDTO;
import kr.ac.kumoh.allimi.dto.user.UserListDTO;
import kr.ac.kumoh.allimi.exception.user.UserAuthException;
import kr.ac.kumoh.allimi.exception.user.UserIdDuplicateException;
import kr.ac.kumoh.allimi.service.NHResidentService;
import lombok.RequiredArgsConstructor;
import kr.ac.kumoh.allimi.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {
    private final UserService userService;
    private final NHResidentService nhResidentService;

    //회원가입
    @PostMapping("/v2/users")
    public ResponseEntity addUser(@RequestBody SignUpDTO dto) { // login_id, password, name, phone_num;
        Long userId;

        try {
            userId = userService.addUser(dto);
        } catch(UserIdDuplicateException exception) { //중복된 id 에러
          log.info("회원가입: 중복된 id로 회원가입");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch(Exception exception) { //그냥 에러
          log.info("회원가입: 회원가입하는데 exception이 발생");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        Map<String, Long> map = new HashMap<>();
        map.put("user_id", userId);

        return ResponseEntity.status(HttpStatus.OK).body(map); // user_id
    }

    @PostMapping("/v2/login") // 로그인
    public ResponseEntity login(@RequestBody LoginDTO dto) {  // login_id, password
        ResponseLogin responseLogin;

        try {
            responseLogin = userService.login(dto.getLogin_id(), dto.getPassword());
        } catch (Exception exception) {
          log.info("일치하는 id, password가 없거나 로그인 중 에러 발생");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); //해당 id password 일치하는 게 없음
        }

        return ResponseEntity.status(HttpStatus.OK).body(responseLogin);  //user_id, userRole
    }

//    @GetMapping("/v2/users/{user_id}") // 사용자 정보 조회
//    public ResponseEntity userInfo(@PathVariable("user_id") Long userId) {
//        if (userId == null) {
//          log.info("사용자 정보조회: user_id가 null로 들어옴. 잘못된 요청");
//          return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//        }
//
//        UserListDTO userListDTO;
//
//        try {
//            userListDTO = userService.getUserInfo(userId);
//        } catch (Exception exception) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); //user가 없는 경우
//        }
//
//        return ResponseEntity.status(HttpStatus.OK).body(userListDTO); //    user_name, tel, id
//    }

//    @DeleteMapping("/v1/users") // 회원 탈퇴
//    public ResponseEntity deleteUser(@RequestBody Map<String, Long> user) {
//        try {
//            userService.deleteUser(user.get("user_id"));
//        }catch (UserAuthException exception) { //user 권한이 없을 때
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
//        }catch (Exception exception) { //user를 찾을 수 없을 때
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//        }
//
//        return ResponseEntity.status(HttpStatus.OK).build();
//    }
//
//    @PostMapping("/v1/logout") // 로그아웃
//    public ResponseEntity logout(@RequestBody Map<String, Long> user) {
////        TODO: 로그아웃
//        return ResponseEntity.status(HttpStatus.OK).build();
//    }
}


