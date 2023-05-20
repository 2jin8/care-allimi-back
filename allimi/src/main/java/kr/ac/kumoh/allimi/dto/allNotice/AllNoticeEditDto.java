package kr.ac.kumoh.allimi.dto.allNotice;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class AllNoticeEditDto {
  @NotNull(message = "공지사항 id가 널이어서는 안됩니다")
  private Long allnotice_id;

  @NotNull(message = "작성자 id가 널이어서는 안됩니다")
  private Long writer_id;

  private String title;
  private String contents;
  private boolean important;
}
