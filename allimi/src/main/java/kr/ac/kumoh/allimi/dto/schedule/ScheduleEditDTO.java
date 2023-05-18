package kr.ac.kumoh.allimi.dto.schedule;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ScheduleEditDTO {
    @NotNull(message = "schedule_id가 널이어서는 안됩니다")
    private Long schedule_id;

    @NotNull(message = "user_id가 널이어서는 안됩니다")
    private Long user_id;

    private LocalDate date;
    private String texts;
}
