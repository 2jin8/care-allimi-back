package kr.ac.kumoh.allimi.dto.notice;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
@NotNull
public class NoticeWriteDto {
  @NotNull(message = "작성자 id가 널이어서는 안됩니다")
  private Long writer_id;

  @NotNull(message = "타겟 id가 널이어서는 안됩니다")
  private Long target_id;

  private String contents;
  private String sub_contents;
}
