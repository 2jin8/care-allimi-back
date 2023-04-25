package kr.ac.kumoh.allimi.repository;

import kr.ac.kumoh.allimi.domain.User;
import kr.ac.kumoh.allimi.domain.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByIdAndPasswords(String id, String passwords);

    Optional<User> findUserByUserId(Long user_id);

    Optional<User> findUserById(String id);
}
