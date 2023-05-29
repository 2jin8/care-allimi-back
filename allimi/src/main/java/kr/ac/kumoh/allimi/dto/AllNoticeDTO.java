package kr.ac.kumoh.allimi.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

public class AllNoticeDTO {

  @Getter
  public static class Write {
    @NotNull(message = "작성자 id가 널이어서는 안됩니다")
    private Long writer_id;

    @NotNull(message = "title이 널이어서는 안됩니다")
    private String title;

    private String contents;
    private boolean important;
  }

  @Getter
  public static class Edit {
    @NotNull(message = "공지사항 id가 널이어서는 안됩니다")
    private Long allnotice_id;

    @NotNull(message = "작성자 id가 널이어서는 안됩니다")
    private Long writer_id;

    private String title;
    private String contents;
    private boolean important;
  }

  @Getter
  @Builder
  public static class ListAll {
    @NotNull(message = "allnoticeId가 널이어서는 안됩니다")
    Long allNoticeId;

    LocalDateTime create_date;
    String title;
    String content;
    Boolean important;
    Long writer_id;
    //@Builder.Default
    java.util.List<String> imageUrl;  // = new ArrayList<String>();
  }

}
