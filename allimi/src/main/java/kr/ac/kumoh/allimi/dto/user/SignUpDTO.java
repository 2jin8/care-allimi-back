package kr.ac.kumoh.allimi.dto.user;

import kr.ac.kumoh.allimi.domain.UserRole;
import lombok.*;

@Getter
@ToString
//@Builder
public class SignUpDTO {
    private String id;
    private String password;
    private String name;
    private String tel;
}
