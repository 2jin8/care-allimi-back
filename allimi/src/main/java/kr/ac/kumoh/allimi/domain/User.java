package kr.ac.kumoh.allimi.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Entity
@Getter
public class User {
    /**
     * user_id
     * id
     * Password
     * Tel
     * name
     * role
     */

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @NotNull
    private String id;

    @NotNull
    private String password;

    private String tel;
    private String name;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    public User(String id, String password, String tel, String name) {
        this.id = id;
        this.password = password;
        this.tel = tel;
        this.name = name;
    }

    public User() {

    }
}
