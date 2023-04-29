package kr.ac.kumoh.allimi.dto.admin;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdminUserListDTO {
  private Long user_id;
    private String user_name;
    private String phone_num;
    private String login_id;

    public AdminUserListDTO(Long userId, String userName, String phoneNum, String loginId) {
      this.user_id = userId;
        this.user_name = userName;
        this.phone_num = phoneNum;
        this.login_id = loginId;
    }
}