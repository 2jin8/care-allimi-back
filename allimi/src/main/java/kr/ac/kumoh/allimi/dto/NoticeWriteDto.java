package kr.ac.kumoh.allimi.dto;

import lombok.Getter;

@Getter
public class NoticeWriteDto {
    private Long userId;
    private Long target;
    private Long facilityId;
    private String contents;
    private String subContents;
}
