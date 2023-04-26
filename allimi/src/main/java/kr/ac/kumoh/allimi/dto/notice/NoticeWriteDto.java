package kr.ac.kumoh.allimi.dto.notice;

import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
//@Builder
public class NoticeWriteDto {
    private Long user_id;
    private Long target_id;  //nhresident_id
    private Long facility_id;
    private String contents;
    private String sub_contents;

}
