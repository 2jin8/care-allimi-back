package kr.ac.kumoh.allimi.domain.func;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import kr.ac.kumoh.allimi.domain.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Entity
public class Notice {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "notice_id")
  private Long noticeId;

  @OneToOne
  @JoinColumn(name = "target_id", referencedColumnName = "nhr_id")
  private NHResident target;

  @NotNull
  @JoinColumn(name = "writer_id")
  @ManyToOne
  private NHResident writer;

  @Column(name = "created_date")
  @Builder.Default
  @CreationTimestamp
  private LocalDateTime createdDate = LocalDateTime.now();

  @Lob
  @Column(length = 100000)
  private String contents;

  @Lob
  @Column(name = "sub_contents", length = 100000)
  private String subContents;

  @OneToMany(mappedBy = "imageId")
  @Builder.Default
  private List<Image> images = new ArrayList<>();

  public static Notice newNotice(@NotNull NHResident writer, @NotNull NHResident target,
                                 String contents, String subContents) {
    Notice ntc = Notice.builder()
            .writer(writer)
            .target(target)
            .contents(contents)
            .subContents(subContents)
            .build();

      return ntc;
  }

  public void setNhResident(NHResident target) {
  this.target = target;
  }

    public void addImages(List<Image> images) {
        this.images = images;
    }

  public void editNotice(NHResident target, String contents, String subContents, List<String> imageUrls) {
      setNhResident(target);
      this.contents = contents;
      this.subContents = subContents;

      this.images = null;

      List<Image> imageList = new ArrayList<>();
      for (String imageUrl : imageUrls) {
          Image i = Image.newNoticeImage(this, imageUrl);
          imageList.add(i);
      }
      this.images = imageList;
  }
}
