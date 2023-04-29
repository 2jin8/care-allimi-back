package kr.ac.kumoh.allimi.dto.letter;

import lombok.Getter;

@Getter
//@Builder
public class LetterWriteDto {
    private Long user_id;
    private Long nhresident_id;
    private Long facility_id;
    private String contents;
}
