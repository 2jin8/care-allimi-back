package kr.ac.kumoh.allimi.dto.nhresident;

import jakarta.validation.constraints.NotNull;
import kr.ac.kumoh.allimi.domain.UserRole;
import lombok.Getter;

@Getter
public class NHResidentDTO {
  @NotNull(message = "user_id가 널이어서는 안됩니다")
  private Long user_id;

  @NotNull(message = "facility_id가 널이어서는 안됩니다")
  private Long facility_id;

  private String resident_name;

  private String birth;

  @NotNull(message = "역할이 널이어서는 안됩니다")
  private UserRole user_role;

  private String health_info;
}
