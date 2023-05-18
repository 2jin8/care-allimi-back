package kr.ac.kumoh.allimi.dto.schedule;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ScheduleWriteDTO {
    @NotNull(message = "user_id가 널이어서는 안됩니다")
    private Long user_id;

    @NotNull(message = "facility_id가 널이어서는 안됩니다")
    private Long facility_id;

    private LocalDate date;
    private String texts;
}
