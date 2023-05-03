package kr.ac.kumoh.allimi.dto.schedule;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ScheduleWriteDTO {

    private Long user_id;
    private Long facility_id;
    private LocalDate date;
    private String texts;
}
