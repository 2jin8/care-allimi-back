package kr.ac.kumoh.allimi.controller.response;

import kr.ac.kumoh.allimi.domain.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ResponseResidentDetail {
      private Long nhr_id;
      private Long facility_id;
      private String resident_name;
      private String facility_name;
      private UserRole user_role;
}