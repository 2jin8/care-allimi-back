package kr.ac.kumoh.allimi.dto.visit;

import jakarta.validation.constraints.NotNull;
import kr.ac.kumoh.allimi.domain.func.VisitState;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class VisitListDTO {
    @NotNull(message = "visit_id가 널이어서는 안됩니다")
    private Long visit_id;

    @NotNull(message = "보호자 id가 널이어서는 안됩니다")
    private Long protector_id;

    private LocalDateTime create_date;
    private LocalDateTime want_date;
    private String residentName;
    private String visitorName;
    private VisitState state;
    private String rejReason;
    private String texts;
}

//  private VisitState state = VisitState.WAITING;
