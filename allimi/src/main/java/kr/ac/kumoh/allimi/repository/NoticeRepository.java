package kr.ac.kumoh.allimi.repository;

import kr.ac.kumoh.allimi.domain.Facility;
import kr.ac.kumoh.allimi.domain.NHResident;
import kr.ac.kumoh.allimi.domain.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
    Optional<List> findAllByFacility(Facility facility);

    Optional<List> findAllByTarget(NHResident target);

    Optional<Notice> findById(Long noticeId);

    Long deleteNoticeById(Long noticeId);

}
