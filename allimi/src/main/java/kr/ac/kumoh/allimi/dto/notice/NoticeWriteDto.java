package kr.ac.kumoh.allimi.dto.notice;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Getter
@NotNull
public class NoticeWriteDto {
  @NotNull(message = "user id는 null이면 안됩니다")
  private Long user_id;
  @NotNull(message = "입소자 id는 null이면 안됩니다")
  private Long nhresident_id;
  @NotNull(message = "시설 id는 null이면 안됩니다")
  private Long facility_id;
  private String contents;
  private String sub_contents;
}
