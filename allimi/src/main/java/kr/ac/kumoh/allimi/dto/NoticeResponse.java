package kr.ac.kumoh.allimi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class NoticeResponse {
    LocalDateTime create_date;
    String content;
}
