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
            return user.getUser_id();

        }

        return null;

    }




}
