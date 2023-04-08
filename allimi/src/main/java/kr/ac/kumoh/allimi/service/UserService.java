package kr.ac.kumoh.allimi.service;


import kr.ac.kumoh.allimi.domain.Facility;
import kr.ac.kumoh.allimi.domain.NHResident;
import kr.ac.kumoh.allimi.dto.NHResidentDTO;
import kr.ac.kumoh.allimi.dto.NHResidentResponse;
import kr.ac.kumoh.allimi.dto.SignUpDTO;
import kr.ac.kumoh.allimi.domain.User;
import kr.ac.kumoh.allimi.domain.UserRole;
import kr.ac.kumoh.allimi.dto.UserListDTO;
import kr.ac.kumoh.allimi.exception.FacilityException;
import kr.ac.kumoh.allimi.exception.UserException;
import kr.ac.kumoh.allimi.exception.UserIdDuplicateException;
import kr.ac.kumoh.allimi.repository.FacilityRepository;
import kr.ac.kumoh.allimi.repository.NHResidentRepository;
import kr.ac.kumoh.allimi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final FacilityRepository facilityRepository;
    private final NHResidentRepository nhResidentRepository;

    @Transactional
    public void addNHResident(NHResidentDTO dto) throws Exception {
//    Long user_id;
//    Long facility_id;
//    String name;
//    UserRole userRole;

        for (UserRole ur: UserRole.values()) {
            if (!ur.name().equals(dto.getUserRole())) {
                new UserException("UserRole이 올바르지 않습니다");
            }
        }

        User user = userRepository.findUserByUserId(dto.getUser_id()).orElseThrow(() -> new UserException("해당 user가 없습니다"));
        Facility facility = facilityRepository.findById(dto.getFacility_id()).orElseThrow(() ->
            new FacilityException("시설을 찾을 수 없습니다")
        );

        NHResident nhResident = NHResident.newNHResident(user, dto.getName(), facility, dto.getUserRole());
        nhResidentRepository.save(nhResident);
    }

    @Transactional
    public Long addUser(SignUpDTO dto) throws Exception {

        // ID 중복 체크
        if (isDuplicateId(dto.getId()))
            new UserIdDuplicateException("중복된 아이디 입니다");

        User user = User.newUser(dto.getId(), dto.getPassword(), dto.getName(), dto.getTel());
        User saved = userRepository.save(user);

        return saved.getUserId();
    }

    public Boolean isDuplicateId(String userId) {
        User user = userRepository.findUserById(userId).orElse(null);

        return (user == null)?false:true;
    }

    public Long login(String userId, String password) throws Exception {
        User user = userRepository.findByIdAndPassword(userId, password)
                .orElseThrow(() -> new UserException("user not found"));

        return user.getUserId();
    }

    @Transactional
    public void deleteUser(Long user_id) throws Exception { // 회원탈퇴
        //user가 있는지 확인
        userRepository.findUserByUserId(user_id).orElseThrow(() -> new UserException("해당 user가 없습니다"));
        userRepository.deleteById(user_id);
    }

    public UserListDTO getUserInfo(Long userId) {
        User user = userRepository.findUserByUserId(userId).orElseThrow(() -> new UserException("해당 user가 없습니다"));

        NHResident nhResident = user.getNhResident().get(user.getCurrentNHResident());

        Facility facility = nhResident.getFacility();

        UserListDTO userListDTO = UserListDTO.builder()
                .facility_name(facility.getName())
                .user_name(user.getName())
                .user_protector_name(nhResident.getName())
                .userRole(nhResident.getUserRole())
                .build();

        return userListDTO;
    }

    public UserRole getUserRole(Long userId) throws Exception {
        User user = userRepository.findUserByUserId(userId)
                .orElseThrow(()-> new UserException("해당 user가 없습니다"));

        return user.getUserRole();
    }

    public User findUser(Long user_id) throws Exception {
        User user = userRepository.findUserByUserId(user_id)
                .orElseThrow(() -> new UserException());

        return user;
    }

//    public List<UserListDTO> getProtectors() {
//        List<User> users = userRepository.findByUserRole(UserRole.PROTECTOR)
//                .orElseGet(() -> new ArrayList<User>());
//
//
//        List<UserListDTO> usersDto = new ArrayList();
//
//        for (User user: users) {
//            System.out.println(user.getUserId());
////            Facility facility = user.getFacility();
//
//            UserListDTO dto = UserListDTO.builder()
//                    .userRole(user.getUserRole())
//                    .user_name(user.getName())
////                    .user_protector_name(user.getProtectorName())
////                    .facility_name(facility.getName())
//                    .build();
//
//            usersDto.add(dto);
//        }
//
//        return usersDto;
//    }
//
//

}