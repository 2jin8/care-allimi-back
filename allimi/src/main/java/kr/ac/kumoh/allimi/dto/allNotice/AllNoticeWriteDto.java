package kr.ac.kumoh.allimi.dto.allNotice;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class AllNoticeWriteDto {
  @NotNull(message = "user_id가 널이어서는 안됩니다")
  private Long user_id;

  @NotNull(message = "facility_id가 널이어서는 안됩니다")
  private Long facility_id;

  @NotNull(message = "title이 널이어서는 안됩니다")
  private String title;

  private String contents;
  private boolean important;
}
