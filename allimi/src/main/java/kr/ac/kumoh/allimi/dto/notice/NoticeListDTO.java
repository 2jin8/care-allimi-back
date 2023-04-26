package kr.ac.kumoh.allimi.dto.notice;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class NoticeListDTO {
    Long noticeId;
    LocalDateTime create_date;
    String content;
    List<String> imageUrl = new ArrayList<>();
}
