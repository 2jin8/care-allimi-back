package kr.ac.kumoh.allimi.controller.response;

import kr.ac.kumoh.allimi.domain.func.VisitState;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class VisitResponse {

    private VisitState state;
    private String texts;
}
