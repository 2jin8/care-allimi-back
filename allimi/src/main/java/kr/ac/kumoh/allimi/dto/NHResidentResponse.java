package kr.ac.kumoh.allimi.dto;

import kr.ac.kumoh.allimi.domain.UserRole;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NHResidentResponse {
    Long resident_id;
    Long facility_id;
    String facility_name;
    String resident_name;
    UserRole userRole;
    boolean isApproved;
}
