package kr.ac.kumoh.allimi.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Entity
@Getter
public class User {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facility_id", referencedColumnName = "facility_id")
    private Facility facility;

    private String name;

    @Column(name = "protector_name")
    private String protectorName;

    @NotNull
    private String id;

    @NotNull
    private String password;

    private String tel;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    public User() {
    }

    public User(Facility facility, String name, String protectorName, String id, String password, String tel, UserRole userRole) {
        this.facility = facility;
        this.name = name;
        this.protectorName = protectorName;
        this.id = id;
        this.password = password;
        this.tel = tel;
        this.userRole = userRole;
    }
}
