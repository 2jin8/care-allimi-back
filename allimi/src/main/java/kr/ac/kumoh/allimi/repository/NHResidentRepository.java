package kr.ac.kumoh.allimi.repository;

import kr.ac.kumoh.allimi.domain.NHResident;
import kr.ac.kumoh.allimi.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface NHResidentRepository extends JpaRepository<NHResident, Long> {

//    Optional<NHResident> findById(Long targetId);

    @Query(value="select * from users where user_id = ?1", nativeQuery = true)
    Optional<List<NHResident>> findByUserId(Long userId);

    @Query(value="select * from nhresident where facility_id = ?1 and is_approved = false", nativeQuery = true)
    Optional<List<NHResident>> findNotApproved(Long facilityId);

    @Query(value = "select * from nhresident where facility_id = ?1", nativeQuery = true)
    Optional<List<NHResident>> findByFacilityId(Long facilityId);

    @Query(value = "select * from nhresident where facility_id = ?1 and user_role = 'PROTECTOR'", nativeQuery = true)
    Optional<List<NHResident>> findProtectorByFacilityId(Long facilityId);

    @Query(value = "select * from nhresident where user_id = ?1 and facility_id = ?2", nativeQuery = true)
    Optional<List<NHResident>> findNHResidentByUserIdAAndFacilityId(Long userId, Long facilityId);

    @Query(value = "select * from nhresident where worker_id = ?1", nativeQuery = true)
    Optional<List<NHResident>> findByWorkerId(Long workerId);

  @Query(value = "select * from nhresident where facility_id = ?1 and user_id = ?2", nativeQuery = true)
  Optional<List<NHResident>> findByFacilityAndUser(Long facilityId, Long userId);
//    @Query("select nhr from NHResident nhr where nhr.userRole = 'PROTECTOR' and nhr.facility.id = ?1")
//    Optional<List> findProtectorByFacilityId(Long facilityId);
}
