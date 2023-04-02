package kr.ac.kumoh.allimi.dto;

import kr.ac.kumoh.allimi.domain.Role;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class UserListDTO{
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