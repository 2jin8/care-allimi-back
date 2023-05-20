package kr.ac.kumoh.allimi.dto.nhresident;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class NHResidentUFDTO {
    @NotNull(message = "nhresident_id가 널이어서는 안됩니다")
    private Long nhresident_id;

    @NotNull(message = "user_id가 널이어서는 안됩니다")
    private Long user_id;

    @NotNull(message = "facility_id가 널이어서는 안됩니다")
    private Long facility_id;
}
