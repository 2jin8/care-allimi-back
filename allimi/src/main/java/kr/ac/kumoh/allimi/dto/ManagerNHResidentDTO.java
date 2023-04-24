package kr.ac.kumoh.allimi.dto;

import kr.ac.kumoh.allimi.domain.UserRole;
import lombok.Getter;

@Getter
//@Builder
public class ManagerNHResidentDTO {
    Long user_id;
    Long facility_id;
    UserRole user_role;
}
