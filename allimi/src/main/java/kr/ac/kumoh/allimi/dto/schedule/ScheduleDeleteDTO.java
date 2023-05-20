package kr.ac.kumoh.allimi.dto.schedule;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ScheduleDeleteDTO {
    @NotNull(message = "schedule_id가 널이어서는 안됩니다")
    private Long schedule_id;

    @NotNull(message = "user_id가 널이어서는 안됩니다")
    private Long user_id;
}
