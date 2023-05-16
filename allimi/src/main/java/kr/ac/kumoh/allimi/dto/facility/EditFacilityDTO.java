package kr.ac.kumoh.allimi.dto.facility;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class EditFacilityDTO {
  @NotNull(message = "facility_id가 널이어서는 안됩니다")
  private Long facility_id;

  private String name;
  private String address;
  private String tel;
  private String fm_name;
}
