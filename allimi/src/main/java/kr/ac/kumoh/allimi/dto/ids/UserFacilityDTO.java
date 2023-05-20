package kr.ac.kumoh.allimi.dto.ids;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UserFacilityDTO {
    @NotNull(message = "user_id가 널이어서는 안됩니다")
    private Long user_id;

    @NotNull(message = "facility_id가 널이어서는 안됩니다")
    private Long facility_id;
}
