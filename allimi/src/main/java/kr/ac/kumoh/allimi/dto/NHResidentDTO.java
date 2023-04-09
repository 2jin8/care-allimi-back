package kr.ac.kumoh.allimi.dto;

import kr.ac.kumoh.allimi.domain.UserRole;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NHResidentDTO {
    Long user_id;
    Long facility_id;
    String name;
    UserRole userRole;

}
