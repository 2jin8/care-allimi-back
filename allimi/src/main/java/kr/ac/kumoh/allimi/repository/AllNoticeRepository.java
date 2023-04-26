package kr.ac.kumoh.allimi.repository;

import kr.ac.kumoh.allimi.domain.AllNotice;
import kr.ac.kumoh.allimi.domain.Visit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AllNoticeRepository extends JpaRepository<AllNotice, Long> {
}