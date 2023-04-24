package kr.ac.kumoh.allimi.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter(AccessLevel.PRIVATE)
@Table(name = "nhresident")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NHResident {
    @Id
    @Column(name = "resident_id")
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

    @Column(name = "is_approved")
    private boolean isApproved = false;

    public static NHResident newNHResident(User user, String name, Facility facility, UserRole userRole, String birth, String healthInfo) {
        NHResident nhResident = new NHResident();
        nhResident.setName(name);
        nhResident.setFacility(facility);
        nhResident.setUser(user);
        nhResident.setUserRole(userRole);
        nhResident.setBirth(birth);
        nhResident.setHealthInfo(healthInfo);

        user.addNHResident(nhResident);
        return nhResident;
    }
}

