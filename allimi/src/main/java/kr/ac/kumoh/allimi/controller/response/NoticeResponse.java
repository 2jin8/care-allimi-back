package kr.ac.kumoh.allimi.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class NoticeResponse {
    Long notice_id;
    Long user_id;
    String writer_name;
    LocalDateTime create_date;
    String content;
    String sub_content;
    List<String> image_url;
}
