package kr.ac.kumoh.allimi.service;

import kr.ac.kumoh.allimi.controller.response.ResponseLogin;
import kr.ac.kumoh.allimi.controller.response.ResponseResidentDetail;
import kr.ac.kumoh.allimi.domain.*;
import kr.ac.kumoh.allimi.dto.admin.AdminUserListDTO;
import kr.ac.kumoh.allimi.dto.nhresident.NHResidentDTO;
import kr.ac.kumoh.allimi.dto.nhresident.NHResidentResponse;
import kr.ac.kumoh.allimi.dto.user.SignUpDTO;
import kr.ac.kumoh.allimi.dto.user.UserListDTO;
import kr.ac.kumoh.allimi.exception.FacilityException;
import kr.ac.kumoh.allimi.exception.NHResidentException;
import kr.ac.kumoh.allimi.exception.user.UserAuthException;
import kr.ac.kumoh.allimi.exception.user.UserException;
import kr.ac.kumoh.allimi.exception.user.UserIdDuplicateException;
import kr.ac.kumoh.allimi.repository.FacilityRepository;
import kr.ac.kumoh.allimi.repository.NHResidentRepository;
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
    private final NHResidentRepository nhResidentRepository;


  public List<AdminUserListDTO> getAllUser() throws Exception {
    List<User> users = userRepository.findAll();
    List<AdminUserListDTO> dtos  = new ArrayList<>();

    for (User user : users) {
      dtos.add(AdminUserListDTO.builder()
              .user_id(user.getUserId())
              .user_name(user.getName())
              .phone_num(user.getPhoneNum())
              .login_id(user.getLoginId())
              .build()
      );
    }

    return dtos;
  }

  @Transactional
  public void changeNHResident(Long user_id, Long nhr_id) throws Exception {
    User user = userRepository.findUserByUserId(user_id)
            .orElseThrow(() -> new UserException("해당하는 user가 없습니다"));

    NHResident nhr = nhResidentRepository.findById(nhr_id)
            .orElseThrow(() -> new NHResidentException("해당하는 입소자가 없습니다"));

    List<NHResident> usersNHR = user.getNhResident();
    if (!usersNHR.contains(nhr)) {
      new NHResidentException("user에 해당하는 입소자가 없습니다");
    }

    user.changeCurrNHResident(nhr_id);
  }

  @Transactional
  public Long addUser(SignUpDTO dto) throws Exception { // login_id, password, name, phone_num;
      // ID 중복 체크
      if (isDuplicateId(dto.getLogin_id()))
          throw new UserIdDuplicateException("중복된 아이디 입니다");

      User user = User.newUser(dto.getLogin_id(), dto.getPassword(), dto.getName(), dto.getPhone_num());
      User saved = userRepository.save(user);

      return saved.getUserId();
  }

  public ResponseResidentDetail getCurrNHResident(Long userId) throws Exception {
    User user = userRepository.findUserByUserId(userId)
            .orElseThrow(() -> new UserException("user not found"));

    NHResident nhResident = nhResidentRepository.findById(user.getCurrentNHResident())
            .orElseThrow(() -> new NHResidentException("resident not found"));

    Facility facility = nhResident.getFacility();

    ResponseResidentDetail response = ResponseResidentDetail.builder()
            .nhr_id(nhResident.getId())
            .facility_id(facility.getId())
            .resident_name(nhResident.getName())
            .facility_name(facility.getName())
            .user_role(nhResident.getUserRole())
            .build();

    return response;
  }

  public Boolean isDuplicateId(String id) {
    User user = userRepository.findUserByLoginId(id).orElse(null);

    return (user == null) ? false : true;
  }

  public ResponseLogin login(String loginId, String password) throws Exception { // login_id, password
      User user = userRepository.findByLoginIdAndPasswords(loginId, password)
              .orElseThrow(() -> new UserAuthException("user not found"));

      if (user.getCurrentNHResident() == null) { //입소자가 한 명도 없음
        return new ResponseLogin(user.getUserId(), null);
      }

      NHResident nhResident = nhResidentRepository.findById(user.getCurrentNHResident())
              .orElseThrow(() -> new NHResidentException("입소자 찾기에서 오류가 발생"));

      return new ResponseLogin(user.getUserId(), nhResident.getUserRole());
  }

  public UserListDTO getUserInfo(Long userId) throws Exception {
    User user = userRepository.findUserByUserId(userId)
            .orElseThrow(() -> new UserException("해당 user가 없습니다"));

    UserListDTO userListDTO = UserListDTO.builder()
            .user_name(user.getName())
            .phone_num(user.getPhoneNum())
            .login_id(user.getLoginId())
            .build();

    return userListDTO;
  }

  @Transactional
  public void deleteUser(Long user_id) throws Exception { // 회원탈퇴
      userRepository.deleteById(user_id);
  }

  public List<NHResidentResponse> getNHResidents(Long userId) {
    User user = userRepository.findUserByUserId(userId).orElseThrow(() -> new UserException("user를 찾을 수 없습니다"));

    List<NHResident> nhResidents = user.getNhResident();
    List<NHResidentResponse> nhResidentResponses = new ArrayList<>();

    for (NHResident nhr: nhResidents) {
          Facility facility = nhr.getFacility();
          nhResidentResponses.add(NHResidentResponse.builder()
                  .resident_id(nhr.getId())
                  .resident_name(nhr.getName())
                  .user_role(nhr.getUserRole())
                  .facility_id(facility.getId())
                  .facility_name(facility.getName())
                  .build());
      }

    return nhResidentResponses;
  }

//
////    public UserRole getUserRole(Long userId) throws Exception {
////        User user = userRepository.findUserByUserId(userId)
////                .orElseThrow(()-> new UserException("해당 user가 없습니다"));
////
////        return user.getUserRole();
////    }
//
//    public User findUser(Long user_id) throws Exception {
//        User user = userRepository.findUserByUserId(user_id)
//                .orElseThrow(() -> new UserException());
//
//        return user;
//    }
}