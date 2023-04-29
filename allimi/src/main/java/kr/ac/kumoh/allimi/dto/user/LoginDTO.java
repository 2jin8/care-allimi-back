package kr.ac.kumoh.allimi.dto.user;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class LoginDTO {
    private String login_id;
    private String password;
}
