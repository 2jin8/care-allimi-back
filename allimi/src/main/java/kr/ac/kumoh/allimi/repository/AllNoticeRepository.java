package kr.ac.kumoh.allimi.repository;

import kr.ac.kumoh.allimi.domain.Facility;
import kr.ac.kumoh.allimi.domain.func.AllNotice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AllNoticeRepository extends JpaRepository<AllNotice, Long> {

  Optional<List> findByFacility(Facility facility);

  Long deleteByAllNoticeId(Long allNoticeId);
}