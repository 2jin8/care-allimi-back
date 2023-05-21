package kr.ac.kumoh.allimi.dto.letter;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class

LetterWriteDto {
    @NotNull(message = "nhresident_id가 널이어서는 안됩니다")
    private Long nhresident_id;

    private String contents;
}
