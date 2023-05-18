package kr.ac.kumoh.allimi.dto.allNotice;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class AllNoticeEditDto {
  @NotNull(message = "allnotice_id가 널이어서는 안됩니다")
  private Long allnotice_id;

  @NotNull(message = "user_id가 널이어서는 안됩니다")
  private Long user_id;

  private String title;
  private String contents;
  private boolean important;
}
