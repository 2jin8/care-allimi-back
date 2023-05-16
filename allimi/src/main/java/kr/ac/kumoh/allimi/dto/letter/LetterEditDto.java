package kr.ac.kumoh.allimi.dto.letter;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class LetterEditDto {
    @NotNull(message = "letter_id가 널이어서는 안됩니다")
    private Long letter_id;

    @NotNull(message = "user_id가 널이어서는 안됩니다")
    private Long user_id;

    @NotNull(message = "nhresident_id가 널이어서는 안됩니다")
    private Long nhresident_id;

    private String contents;
}
