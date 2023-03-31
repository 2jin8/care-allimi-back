package kr.ac.kumoh.allimi.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class User {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long user_id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Facility facility;

    private String name;

    private String protector_name;

    private String id;

    private String password;

    private String tel;

    @Enumerated(EnumType.STRING)
    private Role role;

}
