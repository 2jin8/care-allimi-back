package kr.ac.kumoh.allimi.dto.facility;

import lombok.Getter;

@Getter
public class EditFacilityDTO {
  private Long facility_id;
    private String name;
    private String address;
    private String tel;
    private String fm_name;
}
