package kr.ac.kumoh.allimi.domain.func;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import kr.ac.kumoh.allimi.domain.Facility;
import kr.ac.kumoh.allimi.domain.NHResident;
import kr.ac.kumoh.allimi.domain.User;

import java.time.LocalDateTime;

@Entity
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
  private Facility faciltyId;

  @Column(name = "create_date")
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
    private String rejReson;

    @Enumerated(EnumType.STRING) //대기중, 거절, 승인, 완료
    private VisitState state = VisitState.WAITING;
}
