package kr.ac.kumoh.allimi.dto.notice;

import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Getter
//@Builder
public class NoticeWriteDto {
    private Long user_id;
    private Long target_id;  //nhresident_id
    private Long facility_id;
    private String contents;
    private String sub_contents;
    private List<String> image_url = new ArrayList<>();
}
