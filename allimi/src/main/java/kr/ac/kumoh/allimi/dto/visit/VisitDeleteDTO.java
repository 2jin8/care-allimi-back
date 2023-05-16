package kr.ac.kumoh.allimi.dto.visit;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class VisitDeleteDTO {

    @NotNull(message = "visit_id가 널이어서는 안됩니다")
    private Long visit_id;
}
