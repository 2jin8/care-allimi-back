package kr.ac.kumoh.allimi.dto.nhresident;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class NHResidentEditDTO {
  @NotNull(message = "resident_id가 널이어서는 안됩니다")
  private Long resident_id;

  private String resident_name;
  private String birth;
  private String health_info;
}