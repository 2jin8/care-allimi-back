package kr.ac.kumoh.allimi.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "nhresident")
public class NHResident {

    /**
     * facility_id
     * User_id
     * Name
     * Health_info
     * birth
     */

    @Id
    @Column(name = "resident_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facility_id", referencedColumnName = "facility_id")
    private Facility facility;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    private String name;
    private String health_info;
    private String birth;

}
