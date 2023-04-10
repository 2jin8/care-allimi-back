package kr.ac.kumoh.allimi.repository;

import kr.ac.kumoh.allimi.domain.NHResident;
import kr.ac.kumoh.allimi.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface NHResidentRepository extends JpaRepository<NHResident, Long> {

//    Optional<List> findByFacilityId(Long facilityId);

    Optional<NHResident> findById(Long targetId);

    Optional<NHResident> findByUser(User user);

    @Query("select nhr from NHResident nhr where nhr.userRole = 'PROTECTOR' and nhr.facility.id = ?1")
    Optional<List> findProtectorByFacilityId(Long facilityId);
}
