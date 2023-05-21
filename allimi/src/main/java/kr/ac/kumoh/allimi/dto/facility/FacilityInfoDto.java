package kr.ac.kumoh.allimi.dto.facility;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FacilityInfoDto {
  private String name;
  private String address;
  private String tel;
  private String fm_name;
}