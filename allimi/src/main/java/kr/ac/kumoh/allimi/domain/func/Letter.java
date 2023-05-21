package kr.ac.kumoh.allimi.domain.func;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import kr.ac.kumoh.allimi.domain.NHResident;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

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
  @JoinColumn(name = "protector_id")
  private NHResident protector; //글 쓴 보호자

  @Lob
  @Column(length = 50000)
  private String contents;

  @Column(name = "created_date")
  @Builder.Default
  @CreationTimestamp
  private LocalDateTime createdDate = LocalDateTime.now();

  @Builder.Default
  @Column(name = "is_read", nullable = false) //false로 초기화
  private boolean isRead = false;

  public static Letter newLetter(@NotNull NHResident protector, String contents) {
    Letter letter = Letter.builder()
            .protector(protector)
            .contents(contents)
            .build();

    return letter;
  }

  public void edit(String contents) {
    this.contents = contents;
  }

  public void readCheck() {
    this.isRead = true;
  }
}
