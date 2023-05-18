package kr.ac.kumoh.allimi.domain.func;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import kr.ac.kumoh.allimi.domain.Facility;
import kr.ac.kumoh.allimi.domain.User;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "schedules")
public class Schedule {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facility_id")
    private Facility facility;

    @NotNull
    private LocalDate dates;

    @NotNull
    private String texts;

    public static Schedule newSchedule(@NotNull User user, @NotNull Facility facility, @NotNull LocalDate date, @NotNull String texts) {

        Schedule schedule = Schedule.builder()
                .user(user)
                .facility(facility)
                .dates(date)
                .texts(texts)
                .build();

        return schedule;
    }

    public void editSchedule(@NotNull User user, @NotNull LocalDate date, @NotNull String texts) {
        this.user = user;
        this.dates = date;
        this.texts = texts;
    }
}
