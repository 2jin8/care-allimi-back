package kr.ac.kumoh.allimi.dto.notice;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class NoticeEditDto {
    @NotNull(message = "notice_id가 널이어서는 안됩니다")
    private Long notice_id;

    @NotNull(message = "user_id가 널이어서는 안됩니다")
    private Long user_id;

    @NotNull(message = "resident_id가 널이어서는 안됩니다")
    private Long resident_id;

    private String content;
    private String sub_content;
}
