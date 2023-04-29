package kr.ac.kumoh.allimi.dto.notice;

import kr.ac.kumoh.allimi.domain.User;
import lombok.Getter;

@Getter
public class NoticeEditDto {
    private Long notice_id;
    private Long user_id;
    private Long resident_id;
    private String content;
    private String sub_content;
}
