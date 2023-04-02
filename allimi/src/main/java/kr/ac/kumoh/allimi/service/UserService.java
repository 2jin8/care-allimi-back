package kr.ac.kumoh.allimi.service;


import kr.ac.kumoh.allimi.domain.Facility;
import kr.ac.kumoh.allimi.domain.Notice;
import kr.ac.kumoh.allimi.domain.User;
import kr.ac.kumoh.allimi.domain.UserRole;
import kr.ac.kumoh.allimi.dto.UserListDTO;
import kr.ac.kumoh.allimi.exception.UserException;
import kr.ac.kumoh.allimi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
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

    @Transactional(readOnly = true)
    public UserRole getUserRole(Long userId) {

        User user = userRepository.findUserByUserId(userId)
                .orElseThrow(()-> new UserException());

        return user.getUserRole();

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

    @Transactional(readOnly = true)
    public List<UserListDTO> getProtectors() {
        List<User> users = userRepository.findByUserRole(UserRole.PROTECTOR)
                .orElseGet(() -> new ArrayList<User>());


        List<UserListDTO> usersDto = new ArrayList();

        for (User user: users) {
            System.out.println(user.getUserId());
            Facility facility = user.getFacility();

            UserListDTO dto = UserListDTO.builder()
                    .userRole(user.getUserRole())
                    .user_name(user.getName())
                    .user_protector_name(user.getProtectorName())
                    .facility_name(facility.getName())
                    .build();

            usersDto.add(dto);
        }

        return usersDto;
    }
}
