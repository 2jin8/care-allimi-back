package kr.ac.kumoh.allimi.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class NoticeDTO {
  @Getter
  @NotNull
  public static class Write {
    @NotNull(message = "작성자 id가 널이어서는 안됩니다")
    private Long writer_id;

    @NotNull(message = "타겟 id가 널이어서는 안됩니다")
    private Long target_id;

    private String contents;
    private String sub_contents;
  }

  @Getter
  @Builder
  public static class ListAll {
    @NotNull(message = "noticeId가 널이어서는 안됩니다")
    Long noticeId;

    LocalDateTime create_date;
    String content;
    String writer_name;
    String target_name;
    List<String> imageUrl = new ArrayList<>();
  }

  @Getter
  public static class Edit {
    @NotNull(message = "notice_id가 널이어서는 안됩니다")
    private Long notice_id;

    @NotNull(message = "작성자 id가 널이어서는 안됩니다")
    private Long writer_id;

    @NotNull(message = "타겟 id가 널이어서는 안됩니다")
    private Long target_id;

    private String content;
    private String sub_content;
  }
}
