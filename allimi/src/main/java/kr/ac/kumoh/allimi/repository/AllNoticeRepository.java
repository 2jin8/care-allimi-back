package kr.ac.kumoh.allimi.repository;

import kr.ac.kumoh.allimi.domain.Facility;
import kr.ac.kumoh.allimi.domain.NHResident;
import kr.ac.kumoh.allimi.domain.func.AllNotice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AllNoticeRepository extends JpaRepository<AllNotice, Long> {

  @Query("select an from AllNotice an join NHResident nhr where nhr.facility = ?1")
  Optional<List<AllNotice>> findByFacility(Facility facility);

  Optional<List<AllNotice>> findAllByWriter(NHResident writer);

  Long deleteByAllNoticeId(Long allNoticeId);
}