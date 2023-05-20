package kr.ac.kumoh.allimi.dto.schedule;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ScheduleWriteDTO {
  @NotNull(message = "writer_id가 널이어서는 안됩니다")
  private Long writer_id;

  @NotNull(message = "date가 널이어서는 안됩니다")
  private LocalDate date;

  @NotNull(message = "texts가 널이어서는 안됩니다")
  private String texts;
}
