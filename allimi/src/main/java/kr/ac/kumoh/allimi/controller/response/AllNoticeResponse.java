package kr.ac.kumoh.allimi.controller.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class AllNoticeResponse { //  allNoticeId, user, facility, createDate, title, contents, important, images
    Long allnotice_id;
    Long user_id;
    LocalDateTime create_date;
    String title;
    String content;
    boolean important;
    List<String> image_url;
}
