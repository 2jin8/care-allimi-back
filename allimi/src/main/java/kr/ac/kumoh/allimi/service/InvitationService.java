package kr.ac.kumoh.allimi.service;

import kr.ac.kumoh.allimi.controller.response.ResponseInvitation;
import kr.ac.kumoh.allimi.domain.*;
import kr.ac.kumoh.allimi.dto.invitation.SendInvitationDto;
import kr.ac.kumoh.allimi.exception.*;
import kr.ac.kumoh.allimi.repository.FacilityRepository;
import kr.ac.kumoh.allimi.repository.InvitationRepository;
import kr.ac.kumoh.allimi.repository.NHResidentRepository;
import kr.ac.kumoh.allimi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Transactional
@Slf4j
@Service
public class InvitationService {
  private final InvitationRepository invitationRepository;
  private final NHResidentRepository nhResidentRepository;
  private final UserRepository userRepository;
  private final FacilityRepository facilityRepository;

  public Long sendInvitation(SendInvitationDto dto) throws Exception { //user_id, facility_id, userROle

    //UserRole의 포맷이 정확한지 확인
    if (!EnumUtils.isValidEnum(UserRole.class, dto.getUser_role())) {
      throw new InputException("userrole을 정확하게 입력해주세요");
    }

    User user = userRepository.findUserByUserId(dto.getUser_id())
            .orElseThrow(() -> new NoSuchElementException("해당하는 user가 없습니다"));

    Facility facility = facilityRepository.findById(dto.getFacility_id())
            .orElseThrow(() -> new NoSuchElementException("해당하는 시설이 없습니다"));

    // 직원 + 직원 -> X, 나머지는 O
    UserRole userRole = UserRole.valueOf(dto.getUser_role());
    if (userRole == UserRole.WORKER) {
        // 초대장 확인
        boolean roleExists = facilityAndUserAndRoleExists(dto.getFacility_id(), dto.getUser_id(), dto.getUser_role());
        if (roleExists) {
            log.info("NoticeController 초대 보내기: 중복된 초대장");
            throw new DataAlreadyExistsException("이미 존재하는 초대장");
        }
        // 직원으로 등록되어 있는지 확인
        List<NHResident> nhResidents = nhResidentRepository.findByFacilityAndUserAndUserRole(dto.getFacility_id(), dto.getUser_id(), "WORKER")
                .orElse(new ArrayList<>());
        if (nhResidents.size() != 0) {
            log.info("한 시설에 직원 여러 번 등록 불가능");
            throw new DataAlreadyExistsException2("이미 존재하는 사람");
        }
    }

    Invitation invitation = Invitation.makeInvitation(user, facility, UserRole.valueOf(dto.getUser_role()));
    invitationRepository.save(invitation);

    return invitation.getId();
  }

  public int delete(Long invitId) {
    int deleted = invitationRepository.deleteByInvitId(invitId);

    return deleted;
  }

  public List<ResponseInvitation> findByFacility(Long facilityId) throws Exception {
    Facility facility = facilityRepository.findById(facilityId)
      .orElseThrow(() -> new NoSuchElementException("해당하는 시설이 없음"));

    List<Invitation> invitations = invitationRepository.findByFacilityId(facilityId)
            .orElseGet(() -> new ArrayList<>());

    List<ResponseInvitation> list = new ArrayList<>();

    for (Invitation i: invitations) {
      User user = i.getUser();

      list.add(ResponseInvitation.builder()
              .id(i.getId())
              .user_id(user.getUserId())
              .name(user.getName())
              .userRole(i.getUserRole())
              .date(i.getCreatedDate())
              .build());
    }

    return list;
  }

  public void approve(Long inviteId) throws Exception{
//    invitationRepository.findById(inviteId)
//            .orElseThrow(() -> new NoSuchElementException("해당 초대를 찾을 수 없음"));

    invitationRepository.deleteById(inviteId);
  }

  public boolean facilityAndUserAndRoleExists(Long facilityId, Long userId, String userRole) {
    List<Invitation> invitations = invitationRepository.findByFacilityAndUserAndRole(facilityId, userId, userRole)
            .orElseGet(() -> new ArrayList<>());

    if (invitations.size() > 0)
      return true;
    else
      return false;
  }


  public List<ResponseInvitation> findByUser(Long userId) throws Exception {
    User user = userRepository.findUserByUserId(userId)
      .orElseThrow(() -> new NoSuchElementException("해당하는 user가 없습니다"));

    List<Invitation> invitations = invitationRepository.findByUser(user)
            .orElseGet(() -> new ArrayList<>());

    List<ResponseInvitation> list = new ArrayList<>();

    for (Invitation i : invitations) {
      Facility facility = i.getFacility();

      list.add(ResponseInvitation.builder()
              .id(i.getId())
              .user_id(user.getUserId())
              .facility_id(facility.getId())
              .name(user.getName())
              .facility_name(facility.getName())
              .userRole(i.getUserRole())
              .date(i.getCreatedDate())
              .build());
    }

    return list;
  }

}
