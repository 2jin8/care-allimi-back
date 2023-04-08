package kr.ac.kumoh.allimi.dto;

import kr.ac.kumoh.allimi.domain.UserRole;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
public class SignUpDTO {
    private String id;
    private String password;
    private String name;
    private String tel;
}
