package kr.ac.kumoh.allimi.dto.allNotice;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class AllNoticeListDTO {
    Long allNoticeId;
    LocalDateTime create_date;
    String title;
    String content;
    Boolean important;
    List<String> imageUrl = new ArrayList<>();
}
