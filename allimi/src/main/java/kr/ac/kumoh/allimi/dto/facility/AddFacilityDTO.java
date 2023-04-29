package kr.ac.kumoh.allimi.dto.facility;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class AddFacilityDTO {
    private String name;
    private String address;
    private String tel;
    private String fm_name;
}
