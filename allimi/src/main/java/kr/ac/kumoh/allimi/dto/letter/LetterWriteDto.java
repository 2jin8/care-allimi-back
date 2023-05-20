package kr.ac.kumoh.allimi.dto.letter;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class

LetterWriteDto {
    @NotNull(message = "resident_id가 널이어서는 안됩니다")
    private Long resident_id;

    private String contents;
}
