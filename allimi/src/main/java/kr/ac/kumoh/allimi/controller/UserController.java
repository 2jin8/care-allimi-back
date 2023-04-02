package kr.ac.kumoh.allimi.controller;
import kr.ac.kumoh.allimi.domain.User;
import kr.ac.kumoh.allimi.dto.LoginDTO;
import kr.ac.kumoh.allimi.dto.UserListDTO;
import lombok.RequiredArgsConstructor;
import kr.ac.kumoh.allimi.service.UserService;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/v1/login")
    public ResponseEntity login(@RequestBody LoginDTO dto) {

        Long user_id = userService.login(dto.getId(), dto.getPassword());

        if(user_id == null) {
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


    @PostMapping("/v1/logout") // 실제로는 회원 탈퇴
    public ResponseEntity logout(@RequestBody Map<String, Long> user) {

        boolean logout = userService.logout(user.get("user_id"));
        if (logout) {
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/v1/users/{user_id}")
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
}

