package kr.ac.kumoh.allimi.domain.func;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import kr.ac.kumoh.allimi.domain.Facility;

import kr.ac.kumoh.allimi.domain.NHResident;
import kr.ac.kumoh.allimi.domain.User;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Entity
public class AllNotice {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "allnotice_id")
  private Long allNoticeId;

  @ManyToOne
  @JoinColumn(name = "writer_id")
  private NHResident writer;

  @Column(name = "created_date")
  @Builder.Default
  @CreationTimestamp
  private LocalDateTime createdDate = LocalDateTime.now();

  @Lob
  @Column(length = 5000)
  private String title;

  @Lob
  @Column(length = 100000)
  private String contents;

  @Column(nullable = false) //false로 초기화
  @Builder.Default
  private boolean important = false;

  @OneToMany(mappedBy = "imageId")
  @Builder.Default
  @Column(name = "image_id")
  private List<Image> images = new ArrayList<>();

  public static AllNotice newAllNotice(@NotNull NHResident writer, String title, String contents, Boolean important) {
    AllNotice allNotice = AllNotice.builder()
            .writer(writer)
            .title(title)
            .contents(contents)
            .build();

    if (important != null)
      allNotice.setImportant(important);

    return allNotice;
  }

  public void addImages(List<Image> images) {
    this.images = images;
  }

  public void setImportant(Boolean isImportant) {
    this.important = isImportant;
  }

  //String title, String contents, Boolean important
  public void edit(String title, String contents, Boolean important, List<String> imageUrls) {
    this.title = title;
    this.contents = contents;
    this.important = important;

    this.images = null;

    List<Image> imageList = new ArrayList<>();
    for (String imageUrl : imageUrls) {
      Image i = Image.newAllNoticeImage(this, imageUrl);
      imageList.add(i);
    }
    this.images = imageList;
  }
}
