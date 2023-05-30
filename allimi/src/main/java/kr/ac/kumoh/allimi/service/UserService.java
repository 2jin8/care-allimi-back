package kr.ac.kumoh.allimi.service;

import kr.ac.kumoh.allimi.controller.response.ResponseLogin;
import kr.ac.kumoh.allimi.controller.response.ResponseResidentDetail;
import kr.ac.kumoh.allimi.domain.*;
import kr.ac.kumoh.allimi.dto.admin.UserListAdminDTO;
import kr.ac.kumoh.allimi.dto.nhresident.NHResidentResponse;
import kr.ac.kumoh.allimi.dto.UserDTO;
import kr.ac.kumoh.allimi.exception.InputException;
import kr.ac.kumoh.allimi.exception.user.UserIdDuplicateException;
import kr.ac.kumoh.allimi.repository.FacilityRepository;
import kr.ac.kumoh.allimi.repository.NHResidentRepository;
import kr.ac.kumoh.allimi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final FacilityRepository facilityRepository;
    private final NHResidentRepository nhResidentRepository;

  @Transactional
  public Long addUser(UserDTO.SignUp dto) throws Exception { // login_id, password, name, phone_num;
    final String REGEX = "[0-9]+";        //전화번호에 숫자만 포함되어야함
    if(!dto.getPhone_num().matches(REGEX)) {
      throw new InputException("전화번호 포맷이 이상함");
    }

    // ID 중복 체크
    if (isDuplicateId(dto.getLogin_id()))
      throw new UserIdDuplicateException("중복된 아이디 입니다");

    User user = User.newUser(dto.getLogin_id(), dto.getPassword(), dto.getName(), dto.getPhone_num());
    User saved = userRepository.save(user);

    return saved.getUserId();
  }

  public Boolean isDuplicateId(String id) {
    User user = userRepository.findUserByLoginId(id).orElse(null);

    return (user == null) ? false : true;
  }

  public ResponseLogin login(String loginId, String password) throws Exception { // login_id, password
    User user = userRepository.findByLoginIdAndPasswords(loginId, password)
            .orElseThrow(() -> new NoSuchElementException("사용자 찾기 실패"));

    UserRole userRole = null;

    if (user.getCurrentNhresident() != null) {            //입소자가 한 명도 없음 -> userRole = null로 반환
      userRole = user.getUserRole();
    }

    ResponseLogin responseLogin = ResponseLogin.builder()
            .user_id(user.getUserId())
            .user_name(user.getName())
            .user_role(userRole)
            .login_id(user.getLoginId())
            .phone_num(user.getPhoneNum())
            .build();

    return responseLogin;
  }
  public Page<User> getAllUser(Pageable pageable) throws Exception {
    int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
    pageable = PageRequest.of(page, 10);
    Page<User> users = userRepository.findAll(pageable);


    return users;
  }

  public Page<User> getSearchUser(String searchKeyword, Pageable pageable) throws Exception {
    int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
    pageable = PageRequest.of(page, 10);

    Page<User> users = userRepository.findByNameContaining(searchKeyword, pageable).orElse(null);

    return users;
  }

  public List<UserListAdminDTO> getUserByPhoneNum(String phoneNum) throws Exception {
    List<User> users = userRepository.findByPhoneNum(phoneNum)
            .orElseGet(() -> new ArrayList<>());

    List<UserListAdminDTO> dtos  = new ArrayList<>();

    for (User user : users) {
      dtos.add(UserListAdminDTO.builder()
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
            .orElseThrow(() -> new NoSuchElementException("해당하는 user가 없습니다"));

    NHResident nhResident = nhResidentRepository.findById(nhr_id)
      .orElseThrow(() -> new NoSuchElementException("해당하는 nhResident가 없습니다"));

    if (nhResident.getUser().getUserId() != user.getUserId()) {
      new NoSuchElementException("user에 해당 nhResident가 없습니다");
    }

    user.changeCurrNHResident(nhResident);
  }

  public ResponseResidentDetail getCurrNHResident(Long userId) throws Exception {
    User user = userRepository.findUserByUserId(userId)
            .orElseThrow(() -> new NoSuchElementException("사용자 찾기 실패"));

    NHResident nhResident = user.getCurrentNhresident();

    if (nhResident == null) {
      return ResponseResidentDetail.builder().build();
    }

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


  public UserDTO.List getUserInfo(Long userId) throws Exception {
    User user = userRepository.findUserByUserId(userId)
            .orElseThrow(() -> new NoSuchElementException("해당 user가 없습니다"));

    UserDTO.List userListDTO = UserDTO.List.builder()
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

  @Transactional
  public void edit(UserDTO.Edit dto) throws Exception {
    if (isDuplicateId(dto.getLogin_id()))// ID 중복 체크
      throw new UserIdDuplicateException("중복된 아이디 입니다");

    User user = userRepository.findUserByUserId(dto.getUser_id())
            .orElseThrow(() -> new NoSuchElementException());

    user.edit(dto.getLogin_id(), dto.getPassword(), dto.getName(), dto.getPhone_num());
  }

//  public List<NHResidentResponse> getNHResidentsWithFacility(Long userId) throws Exception {
//    User user = userRepository.findUserByUserId(userId).orElseThrow(() -> new NoSuchElementException("사용자 찾기 실패"));
//    List<NHResident> nhResidents = user.getNhResident();
//
//    List<NHResidentResponse> nhResidentResponses = new ArrayList<>();
//
//    for (NHResident nhr: nhResidents) {
//      Facility facility = nhr.getFacility();
//      String name = "";
//      if (nhr.getUserRole() == UserRole.PROTECTOR) {
//        name = nhr.getName();
//      } else {
//        name = facility.getFmName();
//      }
//
//      nhResidentResponses.add(NHResidentResponse.builder()
//              .resident_id(nhr.getId())
//              .resident_name(name)
//              .user_role(nhr.getUserRole())
//              .facility_id(facility.getId())
//              .facility_name(facility.getName())
//              .build());
//    }
//
//    return nhResidentResponses;
//  }

  public List<NHResidentResponse> getNHResidents(Long userId) throws Exception {
    User user = userRepository.findUserByUserId(userId)
            .orElseThrow(() -> new NoSuchElementException("user를 찾을 수 없습니다"));

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

}