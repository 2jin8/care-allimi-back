package kr.ac.kumoh.allimi.repository;

import kr.ac.kumoh.allimi.domain.Facility;
import kr.ac.kumoh.allimi.domain.NHResident;
import kr.ac.kumoh.allimi.domain.Notice;
import kr.ac.kumoh.allimi.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    @Query("select ntc from Notice ntc where ntc.facility = ?1 order by ntc.createDate desc")
    Optional<List> findAllByFacility(Facility facility);

    @Query("select ntc from Notice ntc where ntc.nhResident = ?1 order by ntc.createDate desc")
    Optional<List> findAllByTarget(NHResident target);

    Optional<Notice> findById(Long noticeId);

    Long deleteNoticeById(Long noticeId);

    void deleteByIdIn(List<Long> ids);
//    Optional<List> findByUserOrTarget(User user, NHResident target);
}
