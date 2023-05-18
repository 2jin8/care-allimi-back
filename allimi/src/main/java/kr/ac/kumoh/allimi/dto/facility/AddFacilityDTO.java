package kr.ac.kumoh.allimi.dto.facility;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddFacilityDTO {
    @NotNull(message = "name이 널이어서는 안됩니다")
    private String name;

    private String address;
    private String tel;
    private String fm_name;
}
