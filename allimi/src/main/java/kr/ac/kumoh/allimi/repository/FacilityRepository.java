package kr.ac.kumoh.allimi.repository;

import kr.ac.kumoh.allimi.domain.Facility;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FacilityRepository extends JpaRepository<Facility, Long> {
    Optional<Page<Facility>> findByNameContaining(String searchKeyword, Pageable pageable);


}
