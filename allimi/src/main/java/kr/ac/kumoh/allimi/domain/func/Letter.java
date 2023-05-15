package kr.ac.kumoh.allimi.domain.func;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import kr.ac.kumoh.allimi.domain.Facility;
import kr.ac.kumoh.allimi.domain.NHResident;
import kr.ac.kumoh.allimi.domain.User;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Entity
public class Letter {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "letter_id")
  private Long letterId;

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

  @Lob
  @Column(length = 50000)
  private String contents;

  @Column(name = "create_date")
  @Builder.Default
  @CreationTimestamp
  private LocalDateTime createDate = LocalDateTime.now();

  @Builder.Default
  @Column(name = "is_read", nullable = false) //false로 초기화
  private boolean isRead = false;

  public static Letter newLetter(@NotNull User user, @NotNull NHResident nhResident, @NotNull Facility facility, String contents) {
    Letter letter = Letter.builder()
            .user(user)
            .nhResident(nhResident)
            .facility(facility)
            .contents(contents)
            .build();

    return letter;
  }

  public void edit(NHResident resident, String contents) {
    this.nhResident = resident;
    this.contents = contents;
  }

  public void readCheck() {
    this.isRead = true;
  }
}
