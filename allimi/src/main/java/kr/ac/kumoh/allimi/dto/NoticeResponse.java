package kr.ac.kumoh.allimi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class NoticeResponse {
    Long noticeId;
    LocalDateTime create_date;
    String content;
    String subContent;
}
