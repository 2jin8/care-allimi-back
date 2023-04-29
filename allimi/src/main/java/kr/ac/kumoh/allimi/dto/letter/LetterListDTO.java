package kr.ac.kumoh.allimi.dto.letter;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class LetterListDTO {
  Long letter_id;
  Long nhreaident_id;
  Long user_id;
  String user_name;
  String nhr_name;
  String content;
  Boolean is_read;
  LocalDateTime create_date;
}
