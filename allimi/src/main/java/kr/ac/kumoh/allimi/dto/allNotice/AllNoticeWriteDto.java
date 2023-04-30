package kr.ac.kumoh.allimi.dto.allNotice;

import lombok.Getter;

@Getter
//@Builder
public class AllNoticeWriteDto {
  private Long user_id;
  private Long facility_id;
  private String title;
  private String contents;
  private boolean important;
}
