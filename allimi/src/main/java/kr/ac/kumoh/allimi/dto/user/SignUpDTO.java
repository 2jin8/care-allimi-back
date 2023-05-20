package kr.ac.kumoh.allimi.dto.user;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignUpDTO {
  @NotNull(message = "login_id가 널이어서는 안됩니다")
  private String login_id;

  @NotNull(message = "password가 널이어서는 안됩니다")
  private String password;

  @NotNull(message = "name이 널이어서는 안됩니다")
  private String name;

  @NotNull(message = "전화번호가 null이면 안됩니다")
  private String phone_num;
}
