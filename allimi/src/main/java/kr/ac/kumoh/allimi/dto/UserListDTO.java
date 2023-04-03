package kr.ac.kumoh.allimi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import kr.ac.kumoh.allimi.domain.UserRole;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserListDTO{
    private String facility_name;
    private String user_name;
    private String user_protector_name;
    private UserRole userRole;

    public UserListDTO(String facility_name, String user_name, String user_protector_name, UserRole userRole) {
        this.facility_name = facility_name;
        this.user_name = user_name;
        this.user_protector_name = user_protector_name;
        this.userRole = userRole;
    }
}