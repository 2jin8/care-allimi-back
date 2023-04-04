package kr.ac.kumoh.allimi.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class User {
    /**
     * user_id
     * Id
     * Password
     * Tel
     * name
     * role
     */

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userid;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "facility_id", referencedColumnName = "facility_id")
//    @NotNull
//    private Facility facility;
//    @Column(name = "protector_name")
//    private String protectorName;

    @NotNull
    private String id;

    @NotNull
    private String password;

    private String tel;
    private String name;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    public User() {
    }

    public User(String id, String password, String tel, String name, UserRole userRole) {
        this.id = id;
        this.password = password;
        this.tel = tel;
        this.name = name;
        this.userRole = userRole;
    }

    // # 삭제할 것
    public User(Facility facility, String name, String protectorName, String id, String password, String tel, UserRole userRole) {
        this.name = name;
        this.id = id;
        this.password = password;
        this.tel = tel;
        this.userRole = userRole;
    }
}
