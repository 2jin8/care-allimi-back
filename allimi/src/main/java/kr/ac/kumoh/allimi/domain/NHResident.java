package kr.ac.kumoh.allimi.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter(AccessLevel.PRIVATE)
@Table(name = "nhresident")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class NHResident {
  @Id
  @Column(name = "nhr_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "facility_id") //name: 내 fn이름, ref: 상대 column 이름
  private Facility facility;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @Column(name = "resident_name")
  private String name; //입소자명

  @Column(name ="user_role")
  @Enumerated(EnumType.STRING)
  private UserRole userRole;

  @Column(name = "health_info", length = 20000)
  @Lob
  private String healthInfo;

  private String birth;

  public static NHResident newNHResident(User user, Facility facility, String name, UserRole userRole, String birth, String healthInfo) {
      NHResident nhResident = NHResident.builder()
              .name(name)
              .user(user)
              .facility(facility)
              .userRole(userRole)
              .birth(birth)
              .healthInfo(healthInfo)
              .build();

      nhResident.id = null;

      user.addNHResident(nhResident);
      return nhResident;
  }

  public void edit(String residentName, String birth, String healthInfo) {
    if (residentName != null)
      this.name = residentName;
    if (birth != null)
      this.birth = birth;
    if (healthInfo != null)
      this.healthInfo = healthInfo;
  }

  @Override
  public boolean equals(Object object) {
      NHResident nhResident = (NHResident) object;

      if (nhResident.getId() == this.id)
        return true;
      else
        return false;
  }
}

