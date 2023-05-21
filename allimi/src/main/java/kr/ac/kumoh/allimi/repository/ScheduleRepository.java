package kr.ac.kumoh.allimi.repository;

import kr.ac.kumoh.allimi.domain.Facility;
import kr.ac.kumoh.allimi.domain.func.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

  @Query(value = "select s from Schedule s join NHResident nhr where s.writer.facility = ?1 order by s.dates desc")
  Optional<List<Schedule>> findAllByFacility(Facility facility);

  @Query(value = "select * from schedules where facility_id = ?1 and dates like ?2%", nativeQuery = true)
  Optional<List<Schedule>> findAllByMonth2(Long facility_id, String yearMonth);

  @Query(value = "select sc from Schedule sc join NHResident nhr where nhr.facility = ?1 and sc.dates between ?2 AND ?3")
  Optional<List<Schedule>> findAllByMonth(Facility facility, LocalDate startDate, LocalDate lastDate);
}
