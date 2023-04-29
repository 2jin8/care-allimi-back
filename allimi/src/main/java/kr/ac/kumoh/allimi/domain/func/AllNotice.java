package kr.ac.kumoh.allimi.domain.func;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import kr.ac.kumoh.allimi.domain.Facility;

import kr.ac.kumoh.allimi.domain.Image;
import kr.ac.kumoh.allimi.domain.NHResident;
import kr.ac.kumoh.allimi.domain.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class AllNotice {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "allnotice_id")
  private Long allnoticeId;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @NotNull
  @ManyToOne
  @JoinColumn(name = "facility_id")
  private Facility facility;

  @Column(name = "create_date")
  @CreationTimestamp
  private LocalDateTime createDate = LocalDateTime.now();

  @Lob
  @Column(length = 5000)
  private String title;

  @Lob
  @Column(length = 100000)
  private String contents;

  @OneToMany(mappedBy = "imageId")
  @Column(name = "image_id")
  private List<Image> images = new ArrayList<>();

  private Boolean important = false;

  public static AllNotice newAllNotice(@NotNull User user, @NotNull Facility facility,
                             String title, String contents, List<Image> images) {
    AllNotice allNotice = AllNotice.builder()
            .user(user)
            .facility(facility)
            .title(title)
            .contents(contents)
            .images(images)
            .build();

    return allNotice;
  }
}
