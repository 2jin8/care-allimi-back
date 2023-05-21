package kr.ac.kumoh.allimi.dto.letter;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class LetterListDTO {
  @NotNull(message = "letter_id가 널이어서는 안됩니다")
  private Long letter_id;

  @NotNull(message = "nhresident_id가 널이어서는 안됩니다")
  private Long nhresident_id;

  private String user_name;
  private String nhr_name;
  private String content;
  private Boolean is_read;
  private LocalDateTime created_date;
}
