package kr.ac.kumoh.allimi.dto.visit;

import kr.ac.kumoh.allimi.domain.func.VisitState;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class VisitListDTO {
    Long visit_id;
    Long user_id;
    Long resident_id;
    LocalDateTime create_date;
    LocalDateTime want_date;
    String texts;
    String phoneNum;
    String residentName;
    String visitorName;
    String rejReason;
    VisitState state;
}

//  private VisitState state = VisitState.WAITING;
