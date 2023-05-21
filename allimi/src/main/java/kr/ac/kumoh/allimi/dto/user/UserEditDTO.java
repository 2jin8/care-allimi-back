package kr.ac.kumoh.allimi.dto.user;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserEditDTO {
  @NotNull(message = "user_id가 널이어서는 안됩니다")
  private Long user_id;

  private String login_id;

  private String password;

  private String name;

  private String phone_num;
}
