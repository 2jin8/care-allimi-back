package kr.ac.kumoh.allimi.service;


import kr.ac.kumoh.allimi.domain.Facility;
import kr.ac.kumoh.allimi.dto.SignUpDTO;
import kr.ac.kumoh.allimi.domain.Notice;
import kr.ac.kumoh.allimi.domain.User;
import kr.ac.kumoh.allimi.domain.UserRole;
import kr.ac.kumoh.allimi.dto.UserListDTO;
import kr.ac.kumoh.allimi.exception.UserException;
import kr.ac.kumoh.allimi.repository.FacilityRepository;
import kr.ac.kumoh.allimi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final FacilityRepository facilityRepository;


    @Transactional
    public Long addUser(SignUpDTO dto) {

        // ID 중복 체크
        User checkUser = userRepository.findUserById(dto.getId()).orElse(null);
        if (checkUser != null) return null;

        Facility facility = facilityRepository.findById(dto.getFacility_id())
                .orElseThrow(() -> new UserException("facility not found"));

        User user = new User(facility, dto.getName(), dto.getProtector_name(), dto.getId(), dto.getPassword(), dto.getTel(), dto.getRole());

        User saved = userRepository.save(user);

        return saved.getUserId();
    }

    @Transactional
    public Long deleteUser(Long user_id) { // 회원탈퇴
        User user = userRepository.findUserByUserId(user_id).orElse(null);

        if (user == null)
            return null;

        Long deletedNum = userRepository.deleteUserByUserId(user_id);
        return deletedNum;
    }

    public Long login(String userId, String password) {
        User user = userRepository.findByIdAndPassword(userId, password)
                .orElseThrow(() -> new UserException("user not found"));

        if (user != null) {
            return user.getUserId();
        }
        return null;
    }

    public UserRole getUserRole(Long userId) {

        User user = userRepository.findUserByUserId(userId)
                .orElseThrow(()-> new UserException());

        return user.getUserRole();
    }

    public User findUser(Long user_id) {
        User user = userRepository.findUserByUserId(user_id)
                .orElseThrow(() -> new UserException());

        if (user != null) {
            return user;
        }

        return null;
    }

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
