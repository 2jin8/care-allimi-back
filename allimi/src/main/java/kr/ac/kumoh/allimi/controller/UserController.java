package kr.ac.kumoh.allimi.controller;
import kr.ac.kumoh.allimi.domain.User;
import kr.ac.kumoh.allimi.domain.UserRole;
import kr.ac.kumoh.allimi.dto.LoginDTO;
import kr.ac.kumoh.allimi.dto.SignUpDTO;
import kr.ac.kumoh.allimi.dto.UserListDTO;
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

    @PostMapping("/v1/users")  // 회원 가입
    public ResponseEntity addUser(@RequestBody SignUpDTO dto) {
        Long userId = userService.addUser(dto);
        if (userId == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseLogin(userId));
    }

    @DeleteMapping("/v1/users") // 회원 탈퇴
    public ResponseEntity deleteUser(@RequestBody Map<String, Long> user) {
        Long deleted = userService.deleteUser(user.get("user_id"));
        if (deleted == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/v1/login") // 로그인
    public ResponseEntity login(@RequestBody LoginDTO dto) {
        Long user_id = userService.login(dto.getId(), dto.getPassword());
        if(user_id == null)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseLogin(user_id));
    }

    @PostMapping("/v1/logout") // 로그아웃
    public ResponseEntity logout(@RequestBody Map<String, Long> user) {

//        TODO: 로그아웃

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Getter
    public class ResponseLogin {

        private Long user_id;

        public ResponseLogin(Long user_id) {
            this.user_id = user_id;
        }
    }

    @GetMapping("/v1/users/{user_id}") // 사용자 정보 조회
    public ResponseEntity user_list(@PathVariable Long user_id) {
        if (user_id == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        User user = userService.findUser(user_id);

        String userName = user.getName() == null ? "":user.getName();
        String protectorName = user.getProtectorName() == null ? "":user.getProtectorName();
        String facilityName = user.getFacility() == null ? "" : user.getFacility().getName();

        UserListDTO userListDTO = new UserListDTO(facilityName, userName, protectorName, user.getUserRole());

        return ResponseEntity.status(HttpStatus.OK).body(userListDTO);
    }

    @GetMapping("/v1/users/protectors") // 보호자 정보 조회
    public ResponseEntity protectors_list() {

        List<UserListDTO> protectorDtos = userService.getProtectors();

        return ResponseEntity.status(HttpStatus.OK).body(protectorDtos);
    }
}


