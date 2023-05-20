package kr.ac.kumoh.allimi.dto.visit;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class VisitEditDTO {
    @NotNull(message = "visit_id가 널이어서는 안됩니다")
    private Long visit_id;

    @NotNull(message = "보호자 id가 널이어서는 안됩니다")
    private Long protector_id;

    private LocalDateTime dateTime;
    private String texts;
}
