package kr.ac.kumoh.allimi.dto.notice;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class NoticeListDTO {
    Long noticeId;
    LocalDateTime create_date;
    String content;
    String imageUrl;
}
