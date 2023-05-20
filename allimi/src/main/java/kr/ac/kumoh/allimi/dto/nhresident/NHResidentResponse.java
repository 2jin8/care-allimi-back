package kr.ac.kumoh.allimi.dto.nhresident;

import jakarta.validation.constraints.NotNull;
import kr.ac.kumoh.allimi.domain.UserRole;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NHResidentResponse {
    @NotNull(message = "resident_id가 널이어서는 안됩니다")
    Long resident_id;

    @NotNull(message = "facility_id가 널이어서는 안됩니다")
    Long facility_id;

    String facility_name;
    String resident_name;
    UserRole user_role;
}
