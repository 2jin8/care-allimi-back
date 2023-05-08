package kr.ac.kumoh.allimi.controller.response;

import kr.ac.kumoh.allimi.domain.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ResponseLogin {
  private Long user_id;
  private UserRole user_role;
  private String user_name;
  private String phone_num;
  private String login_id;
}