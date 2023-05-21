package kr.ac.kumoh.allimi.dto.notice;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class NoticeListDTO {
    @NotNull(message = "noticeId가 널이어서는 안됩니다")
    Long noticeId;

    LocalDateTime create_date;
    String content;
    String writer_name;
    String target_name;
    List<String> imageUrl = new ArrayList<>();
}
