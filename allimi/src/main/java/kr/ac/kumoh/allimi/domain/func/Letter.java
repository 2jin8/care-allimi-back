package kr.ac.kumoh.allimi.domain.func;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import kr.ac.kumoh.allimi.domain.Facility;
import kr.ac.kumoh.allimi.domain.NHResident;
import kr.ac.kumoh.allimi.domain.User;

import java.time.LocalDateTime;

@Entity
public class Letter {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "letter_id")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @NotNull
  @JoinColumn(name = "nhr_id", referencedColumnName = "resident_id")
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
  private LocalDateTime createDate = LocalDateTime.now();

  @Column(name = "is_read")
  private Boolean isRead;
}
