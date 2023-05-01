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
    @Column(name = "login_id")
    private String loginId;

    @NotNull
    private String passwords;

    @Column(name = "phone_num")
    private String phoneNum;

    @Column(name = "user_name")
    private String name; //보호자 이름

    @Column(name = "current_nhresident")
    private Long currentNHResident;

    @OneToMany(mappedBy = "user")
    private List<NHResident> nhResident = new ArrayList<>();

  private User(String loginId, String password, String phoneNum, String name) {
    this.loginId = loginId;
    this.passwords = password;
    this.phoneNum = phoneNum;
    this.name = name;
  }

    public void changeCurrNHResident(Long residentId) {
        this.currentNHResident = residentId;
    }
    public void setResidentNull() {
    this.currentNHResident = null;
  }

    public void addNHResident(NHResident nhResident) {
        this.nhResident.add(nhResident);
    }

    public static User newUser(String id, String password, String name, String phoneNum) {
        User user = new User(id, password, phoneNum, name);
        return user;
    }

//    public UserRole getUserRole() {
//        return this.nhResident.get(this.getCurrentNHResident()).getUserRole();
//    }
}