package kr.ac.kumoh.allimi.dto.admin;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserListAdminDTO {

  @NotNull(message = "user_id가 널이어서는 안됩니다")
  private Long user_id;

  private String user_name;
  private String phone_num;
  private String login_id;

  public UserListAdminDTO(Long userId, String userName, String phoneNum, String loginId) {
    this.user_id = userId;
    this.user_name = userName;
    this.phone_num = phoneNum;
    this.login_id = loginId;
  }
}