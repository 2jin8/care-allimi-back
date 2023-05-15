package kr.ac.kumoh.allimi.domain.func;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import kr.ac.kumoh.allimi.domain.Facility;
import kr.ac.kumoh.allimi.domain.NHResident;
import kr.ac.kumoh.allimi.domain.User;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

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

  @Column(length = 1024)
  private String texts;

  @Column(name = "want_date")
  @NotNull
  private LocalDateTime wantDate;

  @Column(name = "phone_num")
  private String phoneNum;

  @NotNull
  @Column(name = "visitor_name")
  private String visitorName;

  @Column(name = "rej_reason")
  private String rejReason;

  @Builder.Default
  @Enumerated(EnumType.STRING) //대기중, 거절, 승인, 완료
  private VisitState state = VisitState.WAITING;


  public static Visit newVisit(@NotNull User user, @NotNull NHResident nhResident, @NotNull Facility facility, @NotNull String visitorName,
                               String texts, LocalDateTime dateTime) {

    Visit visit = Visit.builder()
            .user(user)
            .nhResident(nhResident)
            .facility(facility)
            .visitorName(visitorName)
            .texts(texts)
            .wantDate(dateTime)
            .state(VisitState.WAITING)
            .build();
    return visit;
  }

  public void editVisit(String texts, LocalDateTime dateTime) {
    this.texts = texts;
    this.wantDate = dateTime;
  }

  public void approvalVisit(VisitState state, String rejReason) {
    this.state = state;
    if (state == VisitState.REJECTED)
      this.rejReason = rejReason;
  }

}
