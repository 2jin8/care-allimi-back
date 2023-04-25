package kr.ac.kumoh.allimi.repository;

import kr.ac.kumoh.allimi.domain.Invitation;
import kr.ac.kumoh.allimi.domain.Visit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvitationRepository extends JpaRepository<Invitation, Long> {
}