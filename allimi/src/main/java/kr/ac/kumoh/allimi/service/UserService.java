package kr.ac.kumoh.allimi.service;


import kr.ac.kumoh.allimi.domain.*;
import kr.ac.kumoh.allimi.dto.NHResidentDTO;
import kr.ac.kumoh.allimi.dto.SignUpDTO;
import kr.ac.kumoh.allimi.dto.UserListDTO;
import kr.ac.kumoh.allimi.exception.FacilityException;
import kr.ac.kumoh.allimi.exception.UserException;
import kr.ac.kumoh.allimi.exception.UserIdDuplicateException;
import kr.ac.kumoh.allimi.repository.FacilityRepository;
import kr.ac.kumoh.allimi.repository.NHResidentRepository;
import kr.ac.kumoh.allimi.repository.NoticeRepository;
import kr.ac.kumoh.allimi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.traversal.NodeIterator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final NoticeRepository noticeRepository;
    private final FacilityRepository facilityRepository;
    private final NHResidentRepository nhResidentRepository;

    @Transactional
    public void addNHResident(NHResidentDTO dto) throws Exception {

//        for (UserRole ur: UserRole.values()) {
//            if (!ur.name().equals(dto.getUserRole())) {
//                throw new UserException("UserRole이 올바르지 않습니다");
//            }
//        }

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
            throw new UserIdDuplicateException("중복된 아이디 입니다");

        User user = User.newUser(dto.getId(), dto.getPassword(), dto.getName(), dto.getTel());
        User saved = userRepository.save(user);

        return saved.getUserId();
    }

    public Boolean isDuplicateId(String userId) {
        User user = userRepository.findUserById(userId).orElse(null);

        return (user == null) ? false : true;
    }

    public Long login(String userId, String password) throws Exception {
        User user = userRepository.findByIdAndPassword(userId, password)
                .orElseThrow(() -> new UserException("user not found"));

        return user.getUserId();
    }

    @Transactional
    public void deleteUser(Long user_id) throws Exception { // 회원탈퇴
        //user가 있는지 확인
        User user = userRepository.findUserByUserId(user_id)
                .orElseThrow(() -> new UserException("해당 user가 없습니다"));

        NHResident target = nhResidentRepository.findByUser(user)
                .orElseThrow(() -> new UserException("해당 target이 없습니다."));

        List<Notice> noticeList = noticeRepository.findByUserOrTarget(user, target).orElse(new ArrayList<Notice>());
//        List<Long> idList = noticeList.stream().map(Notice::getId).collect(Collectors.toList());
//        noticeRepository.deleteByIdIn(idList);

        for (Notice notice : noticeList) {
            noticeRepository.delete(notice);
        }
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
}