package kr.ac.kumoh.allimi.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;
import java.time.LocalDate;

@Entity
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Invitation {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "invit_id")
  private Long id;

  @NotNull
  @Column(name = "user_role")
  @Enumerated(EnumType.STRING)
  private UserRole userRole;

  @NotNull
  @ManyToOne
  @JoinColumn(name = "facility_id")
  private Facility facility;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @CreatedDate
  @CreationTimestamp
  @Column(name = "created_date")
  private LocalDate createdDate = LocalDate.now();

  public static Invitation makeInvitation(User user, Facility facility, UserRole userRole) {
    return new Invitation(null, userRole, facility, user, null);
  }
}
