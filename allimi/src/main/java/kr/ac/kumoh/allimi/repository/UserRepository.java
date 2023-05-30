package kr.ac.kumoh.allimi.repository;

import kr.ac.kumoh.allimi.domain.User;
import kr.ac.kumoh.allimi.domain.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByLoginIdAndPasswords(String loginId, String passwords);

  Optional<User> findUserByLoginId(String loginId);

  Optional<User> findUserByUserId(Long user_id);

  @Query(value="select user_role from nhresident where nhr_id = ?1 and user_id = ?2", nativeQuery = true)
  Optional<UserRole> getUserRole(Long nhrId, Long userId);

  Optional<List<User>> findByPhoneNum(String phoneNum);

  Optional<Page<User>> findByNameContaining(String searchKeyword, Pageable pageable);

}
