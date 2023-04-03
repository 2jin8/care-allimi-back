package kr.ac.kumoh.allimi.dto;

import kr.ac.kumoh.allimi.domain.User;
import lombok.Getter;

@Getter
public class NoticeEditDto {
    private Long userId;
    private Long noticeId;
    private Long targetId;
    private String content;
    private String subContent;
}
