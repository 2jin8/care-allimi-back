package kr.ac.kumoh.allimi.dto.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.*;

public class UserDTO {
  @Getter
  @ToString
  public static class Login {
    @NotNull(message = "login_id가 널이어서는 안됩니다")
    private String login_id;

    @NotNull(message = "password가 널이어서는 안됩니다")
    private String password;
  }

  @Getter
  @ToString
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  public static class SignUp {
    @NotNull(message = "login_id가 널이어서는 안됩니다")
    private String login_id;

    @NotNull(message = "password가 널이어서는 안됩니다")
    private String password;

    @NotNull(message = "name이 널이어서는 안됩니다")
    private String name;

    @NotNull(message = "전화번호가 null이면 안됩니다")
    private String phone_num;
  }

  @Getter
  @ToString
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  public static class Edit {
    @NotNull(message = "user_id가 널이어서는 안됩니다")
    private Long user_id;

    private String login_id;
    private String password;
    private String name;
    private String phone_num;
  }

  @Getter
  @ToString
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @JsonInclude(JsonInclude.Include.NON_NULL)
  public static class List{
    private String user_name;
    private String phone_num;
    private String login_id;
    private String user_role;
  }
}
