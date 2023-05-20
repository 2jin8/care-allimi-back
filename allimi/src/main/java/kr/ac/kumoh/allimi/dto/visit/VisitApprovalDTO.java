package kr.ac.kumoh.allimi.dto.visit;

import jakarta.validation.constraints.NotNull;
import kr.ac.kumoh.allimi.domain.func.VisitState;
import lombok.Getter;

@Getter
public class VisitApprovalDTO {
    @NotNull(message = "visit_id가 널이어서는 안됩니다")
    private Long visit_id;

    private VisitState state;
    private String rejReason;
}
