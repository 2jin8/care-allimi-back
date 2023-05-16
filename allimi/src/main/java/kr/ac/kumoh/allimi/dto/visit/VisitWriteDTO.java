package kr.ac.kumoh.allimi.dto.visit;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class VisitWriteDTO {
    @NotNull(message = "user_id가 널이어서는 안됩니다")
    private Long user_id;

    @NotNull(message = "nhresident_id가 널이어서는 안됩니다")
    private Long nhresident_id;

    @NotNull(message = "facility_id가 널이어서는 안됩니다")
    private Long facility_id;

    private String texts;
    private LocalDateTime dateTime;
}
