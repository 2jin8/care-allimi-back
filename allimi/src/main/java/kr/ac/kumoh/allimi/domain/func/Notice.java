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

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @NotNull
  @JoinColumn(name = "nhr_id")
  @ManyToOne
  private NHResident nhResident;

  @NotNull
  @ManyToOne
  @JoinColumn(name = "facility_id")
  private Facility facility;

  @Column(name = "create_date")
  @Builder.Default
  @CreationTimestamp
  private LocalDateTime createDate = LocalDateTime.now();

  @Lob
  @Column(length = 100000)
  private String contents;

  @Lob
  @Column(name = "sub_contents", length = 100000)
  private String subContents;

  @OneToMany(mappedBy = "imageId")
  @Builder.Default
  private List<Image> images = new ArrayList<>();

  public static Notice newNotice(@NotNull User user, @NotNull NHResident target, @NotNull Facility facility,
                                 String contents, String subContents) {
    Notice ntc = Notice.builder()
            .user(user)
            .nhResident(target)
            .facility(facility)
            .contents(contents)
            .subContents(subContents)
            .build();

      return ntc;
  }

  public void setNhResident(NHResident nhResident) {
  this.nhResident = nhResident;
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
