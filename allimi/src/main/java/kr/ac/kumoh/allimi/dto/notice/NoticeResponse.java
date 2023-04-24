package kr.ac.kumoh.allimi.dto.notice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class NoticeResponse {
    Long notice_id;
    Long user_id;
    LocalDateTime create_date;
    String content;
    String sub_content;
    String image_url;
}
