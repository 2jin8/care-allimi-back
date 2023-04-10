package kr.ac.kumoh.allimi.dao.impl;//package kr.ac.kumoh.allimi.dao.impl;

import kr.ac.kumoh.allimi.dao.UserDAO;
import kr.ac.kumoh.allimi.domain.Notice;
import kr.ac.kumoh.allimi.domain.User;
import kr.ac.kumoh.allimi.repository.NoticeRepository;
import kr.ac.kumoh.allimi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDAOImpl implements UserDAO {

    private final UserRepository userRepository;

    @Override
    public User saveUser(User user) {
        userRepository.save(user);
        return user;
    }

    @Override
    public User getUser(Long userId){
        User user = userRepository.getReferenceById(userId);
        return user;
    }
}
