package kr.ac.kumoh.allimi.repository;

import kr.ac.kumoh.allimi.domain.Invitation;
import kr.ac.kumoh.allimi.domain.NHResident;
import kr.ac.kumoh.allimi.domain.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface InvitationRepository extends JpaRepository<Invitation, Long> {

  @Query(value = "select * from invitation where facility_id = ?1", nativeQuery = true)
  Optional<List<Invitation>> findByFacilityId(Long facilityId);


  @Query(value = "select * from invitation where user_id = ?1", nativeQuery = true)
  Optional<List<Invitation>> findByUserId(Long userId);

  @Modifying
  @Query("delete from Invitation iv where iv.id = ?1")
  int deleteByInvitId(Long invitId);

  @Query(value = "select * from invitation where facility_id = ?1 and user_id = ?2 and user_role = ?3", nativeQuery = true)
  Optional<List<Invitation>> findByFacilityAndUserAndRole(Long facilityId, Long userId, String userRole);
}