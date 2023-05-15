package kr.ac.kumoh.allimi.dto.facility;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class EditFacilityDTO {
  @NotNull
  private Long facility_id;
  private String name;
  private String address;
  private String tel;
  private String fm_name;
}
