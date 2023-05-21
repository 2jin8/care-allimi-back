package kr.ac.kumoh.allimi.dto.nhresident;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class NHResidentUFDTO {
    @NotNull(message = "resdient_id가 널이어서는 안됩니다")
    private Long resdient_id;

    @NotNull(message = "worker_id가 널이어서는 안됩니다")
    private Long worker_id;

    @NotNull(message = "facility_id가 널이어서는 안됩니다")
    private Long facility_id;
}
