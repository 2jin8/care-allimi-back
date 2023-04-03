package kr.ac.kumoh.allimi.dto;

import kr.ac.kumoh.allimi.domain.UserRole;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class SignUpDTO {

    private String id;
    private String password;
    private String name;
    private String protector_name;
    private UserRole role;
    private String tel;
    private Long facility_id;
}
