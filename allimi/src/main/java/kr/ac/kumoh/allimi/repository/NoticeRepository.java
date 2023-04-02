package kr.ac.kumoh.allimi.repository;

import kr.ac.kumoh.allimi.domain.Notice;
import kr.ac.kumoh.allimi.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    Optional<List> findByUser(User user);
    Optional<List> findByTarget(User target);

    Optional<Notice> findByNoticeId(Long noticeId);

}
