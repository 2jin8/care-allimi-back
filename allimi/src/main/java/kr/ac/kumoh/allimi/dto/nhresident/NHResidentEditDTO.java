package kr.ac.kumoh.allimi.dto.nhresident;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NHResidentEditDTO {
  Long resident_id;
  String resident_name;
  String birth;
  String health_info;
}