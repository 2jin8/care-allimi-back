package kr.ac.kumoh.allimi.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter(AccessLevel.PROTECTED)
@AllArgsConstructor
@NoArgsConstructor
abstract class Functions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    @JoinColumn(name = "nhr_id", referencedColumnName = "resident_id")
    @ManyToOne
    private NHResident nhResident;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "facility_id")
    private Facility facility;

    @Column(name = "create_date")
    private LocalDateTime createDate = LocalDateTime.now();

    public void setNhResident(NHResident nhResident) {
        this.nhResident = nhResident;
    }
}
