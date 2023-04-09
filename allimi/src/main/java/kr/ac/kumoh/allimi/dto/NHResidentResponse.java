package kr.ac.kumoh.allimi.dto;

import kr.ac.kumoh.allimi.domain.UserRole;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NHResidentResponse {
    Long user_id;
    String name;
    UserRole userRole;

}
