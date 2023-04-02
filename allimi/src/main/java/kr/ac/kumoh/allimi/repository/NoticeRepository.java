package kr.ac.kumoh.allimi.repository;

import kr.ac.kumoh.allimi.domain.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {



}
