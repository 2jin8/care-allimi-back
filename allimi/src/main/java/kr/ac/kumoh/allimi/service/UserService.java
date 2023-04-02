package kr.ac.kumoh.allimi.service;


import kr.ac.kumoh.allimi.domain.User;
import kr.ac.kumoh.allimi.exception.UserException;
import kr.ac.kumoh.allimi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Transactional(readOnly = true)
    public Long login(String userId, String password) {
        User user = userRepository.findByIdAndPassword(userId, password)
                .orElseThrow(() -> new UserException());

        if (user != null) {
            return user.getUserId();
        }

        return null;
    }

    public void logout(Long user_id) {
        userRepository.deleteUserByUserId(user_id)
                .orElseThrow(() -> new UserException());

    }

    @Transactional(readOnly = true)
    public User findUser(Long user_id) {
        User user = userRepository.findUserByUserId(user_id)
                .orElseThrow(() -> new UserException());

        if (user != null) {
            return user;
        }

        return null;
    }
}
