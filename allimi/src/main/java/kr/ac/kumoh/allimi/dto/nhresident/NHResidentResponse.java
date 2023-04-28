package kr.ac.kumoh.allimi.dto.nhresident;

import com.fasterxml.jackson.annotation.JsonInclude;
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
    UserRole user_role;
}
