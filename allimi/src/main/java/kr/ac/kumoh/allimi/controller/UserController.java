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

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/v1/login")
    public ResponseEntity login(@RequestBody LoginDTO dto) {
        Long user_id = userService.login(dto.getId(), dto.getPassword());

        if(user_id == null) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
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

        String userName = user.getName() == null ? "":user.getName();
        String protectorName = user.getProtectorName() == null ? "":user.getProtectorName();
        String facilityName = user.getFacility() == null ? "" : user.getFacility().getName();

        UserListDTO userListDTO = new UserListDTO(facilityName, userName, protectorName, user.getUserRole());

        return ResponseEntity.status(HttpStatus.OK).body(userListDTO);
    }

    @GetMapping("/v1/users/protectors")
    public ResponseEntity protectors_list() {

        List<UserListDTO> protectorDtos = userService.getProtectors();

        return ResponseEntity.status(HttpStatus.OK).body(protectorDtos);
    }
}



