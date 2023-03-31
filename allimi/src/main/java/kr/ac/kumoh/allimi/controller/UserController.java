package kr.ac.kumoh.allimi.controller;

import kr.ac.kumoh.allimi.service.UserService;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@Getter
@ToString
class LoginDTO {
    private String id;
    private String password;
}


@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/v1/login")
    public ResponseEntity login(@RequestBody LoginDTO dto) {

        Long user_id = userService.login(dto.getId(), dto.getPassword());

        if(user_id == null) {
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





}
