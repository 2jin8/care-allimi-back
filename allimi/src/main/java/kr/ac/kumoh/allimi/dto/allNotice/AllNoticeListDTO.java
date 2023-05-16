package kr.ac.kumoh.allimi.dto.allNotice;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class AllNoticeListDTO {
    @NotNull(message = "allnoticeId가 널이어서는 안됩니다")
    Long allNoticeId;

    LocalDateTime create_date;
    String title;
    String content;
    Boolean important;
    List<String> imageUrl = new ArrayList<>();
}
