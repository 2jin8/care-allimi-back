package kr.ac.kumoh.allimi.dto.visit;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class VisitEditDTO {
    private Long visit_id;
    private Long user_id;
    private Long nhresident_id;
    private LocalDateTime dateTime;
    private String texts;
}
