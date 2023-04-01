package kr.ac.kumoh.allimi.controller;

import kr.ac.kumoh.allimi.domain.Role;
import kr.ac.kumoh.allimi.domain.User;
import kr.ac.kumoh.allimi.service.UserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@Getter
@ToString
class LoginDTO {
    private String id;
    private String password;
}

@Getter
@ToString
class UserListDTO{
    private String facility_name;
    private String user_name;
    private String user_protector_name;
    private Role role;

    public UserListDTO(String facility_name, String user_name, String user_protector_name, Role role) {
        this.facility_name = facility_name;
        this.user_name = user_name;
        this.user_protector_name = user_protector_name;
        this.role = role;
    }
}

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/v1/login")
    public ResponseEntity login(@RequestBody LoginDTO dto) {

        Long user_id = userService.login(dto.getId(), dto.getPassword());

        if (user_id == null) {
            System.out.println("@@@@배고파");
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }

        System.out.println("@@@@배만히고파");

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseLogin(user_id));
    }

    @Getter
    public class ResponseLogin {
        private Long user_id;

        public ResponseLogin(Long user_id) {
            this.user_id = user_id;
        }
    }


    @PostMapping("/v1/logout")
    public ResponseEntity logout(@RequestBody Map<String, Long> user) {
        userService.logout(user.get("user_id"));
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/v1/users/{user_id}")
    public ResponseEntity user_list(@PathVariable Long user_id) {
        if (user_id == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        User user = userService.findUser(user_id);

        return ResponseEntity.status(HttpStatus.OK).body(
                new UserListDTO(user.getFacility().getName(), user.getName(), user.getProtector_name(), user.getRole())
        );
    }
}

