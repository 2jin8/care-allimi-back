package kr.ac.kumoh.allimi.repository;

import kr.ac.kumoh.allimi.domain.Facility;
import kr.ac.kumoh.allimi.domain.func.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

  @Query(value = "select * from schedules where facility_id = ?1 and dates like ?2%", nativeQuery = true)
  Optional<List<Schedule>> findAllByMonth(Long facility_id, String yearMonth);
}
