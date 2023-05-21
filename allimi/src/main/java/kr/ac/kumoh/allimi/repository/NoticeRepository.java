package kr.ac.kumoh.allimi.repository;

import kr.ac.kumoh.allimi.domain.Facility;
import kr.ac.kumoh.allimi.domain.NHResident;
import kr.ac.kumoh.allimi.domain.User;
import kr.ac.kumoh.allimi.domain.func.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    @Query("select ntc from Notice ntc where ntc.target = ?1 order by ntc.createdDate desc")
    Optional<List<Notice>> findAllByTarget(NHResident target);

    Optional<Notice> findNoticeByNoticeId(Long noticeId);

    Long deleteNoticeByNoticeId(Long noticeId);

}
