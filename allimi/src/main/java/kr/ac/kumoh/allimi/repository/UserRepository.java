package kr.ac.kumoh.allimi.repository;

import kr.ac.kumoh.allimi.domain.User;
import kr.ac.kumoh.allimi.domain.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByIdAndPassword(String id, String password);

    Optional<User> findUserByUserId(Long user_id);

    Optional<User> findUserById(String id);

//    Long deleteUserByUserId(Long user_id);

}
