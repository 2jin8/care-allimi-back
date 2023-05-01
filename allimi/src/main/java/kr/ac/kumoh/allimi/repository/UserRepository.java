package kr.ac.kumoh.allimi.repository;

import kr.ac.kumoh.allimi.domain.User;
import kr.ac.kumoh.allimi.domain.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByLoginIdAndPasswords(String loginId, String passwords);

  Optional<User> findUserByLoginId(String loginId);

  Optional<User> findUserByUserId(Long user_id);

  @Query(value="select user_role from nhresident where user_id = ?1", nativeQuery = true)
  Optional<List<UserRole>> getUserRoles(Long userId);

  @Query(value="select user_role from nhresident where user_id = ?1", nativeQuery = true)
  Optional<List<UserRole>> getUserRole(Long userId);
}
