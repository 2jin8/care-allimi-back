package kr.ac.kumoh.allimi.domain.func;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import kr.ac.kumoh.allimi.domain.Facility;
import kr.ac.kumoh.allimi.domain.NHResident;
import kr.ac.kumoh.allimi.domain.User;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Visit {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name= "visit_id")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "protector_id", referencedColumnName = "nhr_id")
  private NHResident protector; //면회신청을 한 사람

  @Column(name = "created_date")
  @Builder.Default
  @CreationTimestamp
  private LocalDateTime createdDate = LocalDateTime.now();

  @Column(length = 1024)
  private String texts;

  @Column(name = "want_date")
  @NotNull
  private LocalDateTime wantDate;

  @Column(name = "rej_reason")
  private String rejReason;

  @Builder.Default
  @Enumerated(EnumType.STRING) //대기중, 거절, 승인, 완료
  private VisitState state = VisitState.WAITING;

  public static Visit newVisit(@NotNull NHResident protector, String texts, LocalDateTime wantDateTime) {
    Visit visit = Visit.builder()
            .protector(protector)
            .texts(texts)
            .wantDate(wantDateTime)
            .build();
    return visit;
  }

  public void editVisit(String texts, LocalDateTime wantDateTime) {
    this.texts = texts;
    this.wantDate = wantDateTime;
  }

  public void approvalVisit(VisitState state, String rejReason) {
    this.state = state;
    if (state == VisitState.REJECTED)
      this.rejReason = rejReason;
  }
}
