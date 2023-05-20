package kr.ac.kumoh.allimi.domain.func;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import kr.ac.kumoh.allimi.domain.Facility;
import kr.ac.kumoh.allimi.domain.NHResident;
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
    @JoinColumn(name = "writer_id")
    private NHResident writer;

    @NotNull
    private LocalDate dates;

    @NotNull
    private String texts;

    public static Schedule newSchedule(@NotNull NHResident writer, @NotNull LocalDate date, @NotNull String texts) {
      Schedule schedule = Schedule.builder()
              .writer(writer)
              .dates(date)
              .texts(texts)
              .build();

      return schedule;
    }

    public void editSchedule(@NotNull NHResident writer, @NotNull LocalDate date, @NotNull String texts) {
        this.writer = writer;
        this.dates = date;
        this.texts = texts;
    }
}
