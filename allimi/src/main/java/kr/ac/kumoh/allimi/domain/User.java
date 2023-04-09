package kr.ac.kumoh.allimi.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name="users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
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

    @Column(name = "current_nhresident")
    private int currentNHResident = 0;

    @OneToMany(mappedBy = "user")
    private List<NHResident> nhResident = new ArrayList<>();

    public void changeCurrNHResident(int idx) {
        this.currentNHResident = idx;
    }

    private User(String id, String password, String tel, String name) {
        this.id = id;
        this.password = password;
        this.tel = tel;
        this.name = name;
    }

    public void addNHResident(NHResident nhResident) {
        this.nhResident.add(nhResident);
    }

    public static User newUser(String id, String password, String name, String tel) {
        User user = new User(id, password, tel, name);

        return user;
    }

    public UserRole getUserRole() {
        return this.nhResident.get(this.getCurrentNHResident()).getUserRole();
    }
}