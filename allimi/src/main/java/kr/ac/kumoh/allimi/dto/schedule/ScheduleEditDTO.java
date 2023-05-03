package kr.ac.kumoh.allimi.dto.schedule;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ScheduleEditDTO {
    private Long schedule_id;
    private Long user_id;
    private LocalDate date;
    private String texts;
}
