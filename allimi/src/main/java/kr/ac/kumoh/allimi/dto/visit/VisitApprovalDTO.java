package kr.ac.kumoh.allimi.dto.visit;

import kr.ac.kumoh.allimi.domain.func.VisitState;
import lombok.Getter;

@Getter
public class VisitApprovalDTO {

    private Long visit_id;
    private VisitState state;
    private String rejReason;
}
