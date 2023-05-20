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

    @NotNull(message = "user_id가 널이어서는 안됩니다")
    private Long user_id;

    @NotNull(message = "resident_id가 널이어서는 안됩니다")
    private Long resident_id;

    private LocalDateTime create_date;
    private LocalDateTime want_date;
    private String texts;
    private String phoneNum;
    private String residentName;
    private String visitorName;
    private String rejReason;
    private VisitState state;
}

//  private VisitState state = VisitState.WAITING;
