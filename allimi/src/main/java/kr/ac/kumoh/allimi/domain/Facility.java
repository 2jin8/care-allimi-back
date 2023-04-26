package kr.ac.kumoh.allimi.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Facility {
    @Id
    @Column(name = "facility_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "facility_name")
    private String name;

    @NotNull
    private String address;

    @NotNull
    private String tel;

    @Column(name = "fm_name")
    private String fmName; //시설장 이름

    public static Facility makeFacility(String name, String address, String tel, String fmName) {
        return new Facility(null, name, address, tel, fmName);
    }
}