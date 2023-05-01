package kr.ac.kumoh.allimi.dto.visit;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class VisitWriteDTO {
    private Long user_id;
    private Long nhresident_id;
    private Long facility_id;
    private String texts;
    private LocalDateTime dateTime;
}
