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
    private String user_name;
    private String tel;
    private String id;

    public UserListDTO(String user_name, String tel, String id) {
        this.user_name = user_name;
        this.tel = tel;
        this.id = id;
    }
}