package kr.ac.kumoh.allimi.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "nhresident")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NHResident {

    @Id
    @Column(name = "resident_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facility_id", referencedColumnName = "facility_id")
    private Facility facility;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String name; //입소자명
    private String health_info;
    private String birth;

    @Column(name ="user_role")
    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    public static NHResident newNHResident(User user, String name, Facility facility, UserRole userRole) {
        NHResident nhResident = new NHResident(null, facility, user, name, null, null, userRole);
        user.addNHResident(nhResident);
        return nhResident;
    }
}

