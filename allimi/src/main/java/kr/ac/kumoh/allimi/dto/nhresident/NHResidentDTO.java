package kr.ac.kumoh.allimi.dto.nhresident;

import kr.ac.kumoh.allimi.domain.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
//@Builder
public class NHResidentDTO {
    Long user_id;
    Long facility_id;
    String resident_name;
    String birth;
    UserRole user_role;
    String health_info;
}
