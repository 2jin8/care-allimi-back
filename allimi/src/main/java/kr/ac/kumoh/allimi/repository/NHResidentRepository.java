package kr.ac.kumoh.allimi.repository;

import kr.ac.kumoh.allimi.domain.NHResident;
import kr.ac.kumoh.allimi.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NHResidentRepository extends JpaRepository<NHResident, Long> {

    Optional<NHResident> findByUser(User user);
}
