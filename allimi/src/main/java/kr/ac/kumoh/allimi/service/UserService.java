package kr.ac.kumoh.allimi.service;


import kr.ac.kumoh.allimi.domain.Facility;
import kr.ac.kumoh.allimi.domain.User;
import kr.ac.kumoh.allimi.domain.UserRole;
import kr.ac.kumoh.allimi.dto.SignUpDTO;
import kr.ac.kumoh.allimi.exception.UserException;
import kr.ac.kumoh.allimi.repository.FacilityRepository;
import kr.ac.kumoh.allimi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final FacilityRepository facilityRepository;


    public Long signUp(SignUpDTO dto) {

        User checkUser = userRepository.findUserById(dto.getId()).orElse(null);
        if (checkUser != null) return null;

        Facility facility = facilityRepository.findById(dto.getFacility_id())
                .orElseThrow(() -> new UserException("facility not found"));

        User user = new User(facility, dto.getName(), dto.getProtector_name(), dto.getId(), dto.getPassword(), dto.getTel(), dto.getRole());

        User saved = userRepository.save(user);

        return saved.getUserId();
    }

    @Transactional(readOnly = true)
    public Long login(String userId, String password) {
        User user = userRepository.findByIdAndPassword(userId, password)
                .orElseThrow(() -> new UserException("user not found"));

        if (user != null) {
            return user.getUserId();
        }
        return null;
    }

    public boolean logout(Long user_id) {

        List<User> users = userRepository.deleteUserByUserId(user_id);
        if (users.size() == 0)
            return false;

        return true;
    }

    public UserRole getUserRole(Long userId) {

        User user = userRepository.findUserByUserId(userId)
                .orElseThrow(()-> new UserException());

        System.out.println(user.getUserRole());
        return user.getUserRole();
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
