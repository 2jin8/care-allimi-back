package kr.ac.kumoh.allimi.controller.response;

import kr.ac.kumoh.allimi.domain.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ResponseResident {
      private Long id;
      private Long user_id;
      private String name; //입소자명
      private UserRole user_role;
}