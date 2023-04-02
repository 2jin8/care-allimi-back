package kr.ac.kumoh.allimi.repository;

import kr.ac.kumoh.allimi.domain.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    Long deleteNoticeByNoticeId(Long notice_id);

}
