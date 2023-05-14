package kr.ac.kumoh.allimi.controller.response;

import kr.ac.kumoh.allimi.domain.UserRole;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NHResidentDetailResponse {
    String resident_name;
    String birth;
    String protector_name;
    String protector_phone_num;
}
