package kr.ac.kumoh.allimi.dto;

import lombok.Getter;

@Getter
public class NoticeEditDto {

    private Long userId;
    private Long noticeId;
    private Long ncId;
    private Long targetId;
    private Long facilityId;
    private String content;
    private String subContent;
}
