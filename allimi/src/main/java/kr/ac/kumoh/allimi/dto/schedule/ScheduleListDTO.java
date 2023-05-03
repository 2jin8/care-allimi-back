package kr.ac.kumoh.allimi.dto.schedule;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class ScheduleListDTO {

    private Long schedule_id;
    private LocalDate date;
    private String texts;
}
