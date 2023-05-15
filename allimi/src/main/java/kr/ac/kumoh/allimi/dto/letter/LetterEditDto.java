package kr.ac.kumoh.allimi.dto.letter;

import lombok.Getter;

@Getter
//@Builder
public class LetterEditDto {
    private Long letter_id;
    private Long user_id;
    private Long nhresident_id;
    private String contents;
}
