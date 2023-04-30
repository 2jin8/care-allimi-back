package kr.ac.kumoh.allimi.dto.allNotice;

import lombok.Getter;

@Getter
//@Builder
public class AllNoticeEditDto {
  private Long allnotice_id;
  private Long user_id;
  private String title;
  private String contents;
  private boolean important;
}
