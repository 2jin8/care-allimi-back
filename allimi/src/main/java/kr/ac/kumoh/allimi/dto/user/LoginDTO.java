package kr.ac.kumoh.allimi.dto.user;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class LoginDTO {
  @NotNull(message = "login_id가 널이어서는 안됩니다")
  private String login_id;

  @NotNull(message = "password가 널이어서는 안됩니다")
  private String password;
}
