package kr.ac.kumoh.allimi.dto.user;

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
    private String user_name;
    private String phone_num;
    private String login_id;
    private String user_role;

    public UserListDTO(String userName, String phoneNum, String loginId, String userRole) {
        this.user_name = userName;
        this.phone_num = phoneNum;
        this.login_id = loginId;
        this.user_role = userRole;
    }
}