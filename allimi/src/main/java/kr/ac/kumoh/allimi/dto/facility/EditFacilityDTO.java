package kr.ac.kumoh.allimi.dto.facility;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class EditFacilityDTO {
  @NotNull(message = "시설 id가 널이어서는 안됩니다")
  private Long facility_id;
  private String name;
  private String address;
  private String tel;
  private String fm_name;
}
