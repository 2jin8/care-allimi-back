package kr.ac.kumoh.allimi.controller.response;

import kr.ac.kumoh.allimi.domain.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@Builder
public class ResponseInvitation {
      private Long id;
      private Long user_id;
      private Long facility_id;
      private String name; //입소자명
      private String facility_name;
      private UserRole userRole;
      private LocalDate date;
}