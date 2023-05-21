package kr.ac.kumoh.allimi.dto.notice;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class NoticeEditDto {
    @NotNull(message = "notice_id가 널이어서는 안됩니다")
    private Long notice_id;

    @NotNull(message = "작성자 id가 널이어서는 안됩니다")
    private Long writer_id;

    @NotNull(message = "타겟 id가 널이어서는 안됩니다")
    private Long target_id;

    private String content;
    private String sub_content;
}
