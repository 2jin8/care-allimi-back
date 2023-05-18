package kr.ac.kumoh.allimi.dto.letter;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class LetterListDTO {
  @NotNull(message = "letter_id가 널이어서는 안됩니다")
  Long letter_id;

  @NotNull(message = "nhresident_id가 널이어서는 안됩니다")
  Long nhreaident_id;

  @NotNull(message = "user_id가 널이어서는 안됩니다")
  Long user_id;

  String user_name;
  String nhr_name;
  String content;
  Boolean is_read;
  LocalDateTime create_date;
}
