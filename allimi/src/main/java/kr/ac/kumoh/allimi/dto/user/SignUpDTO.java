package kr.ac.kumoh.allimi.dto.user;

import kr.ac.kumoh.allimi.domain.UserRole;
import lombok.*;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignUpDTO {
  private String login_id;
  private String password;
  private String name;
  private String phone_num;
}
