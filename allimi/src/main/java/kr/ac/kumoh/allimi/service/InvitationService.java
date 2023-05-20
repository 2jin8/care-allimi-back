package kr.ac.kumoh.allimi.service;

import kr.ac.kumoh.allimi.controller.response.ResponseInvitation;
import kr.ac.kumoh.allimi.controller.response.ResponseResident;
import kr.ac.kumoh.allimi.domain.*;
import kr.ac.kumoh.allimi.dto.facility.AddFacilityDTO;
import kr.ac.kumoh.allimi.dto.facility.EditFacilityDTO;
import kr.ac.kumoh.allimi.dto.invitation.SendInvitationDto;
import kr.ac.kumoh.allimi.exception.*;
import kr.ac.kumoh.allimi.exception.user.UserException;
import kr.ac.kumoh.allimi.repository.FacilityRepository;
import kr.ac.kumoh.allimi.repository.InvitationRepository;
import kr.ac.kumoh.allimi.repository.NHResidentRepository;
import kr.ac.kumoh.allimi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Array;
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

    //중복초대 방지
    boolean hasData = facilityAndUserAndRoleExists(dto.getFacility_id(), dto.getUser_id(), UserRole.valueOf(dto.getUser_role()));
    if (hasData) {
      log.info("NoticeController 초대보내기: 중복된 초대장");
      throw new DataAlreadyExistsException("이미 있는 초대장");
    }

    //이미 존재하는 사람 초대 방지
    List<NHResident> data = nhResidentRepository.findByFacilityAndUserAndUserRole(dto.getFacility_id(), dto.getUser_id(), dto.getUser_role())
      .orElseGet(() -> new ArrayList<>());
    if (data.size() != 0) {
      log.info("NoticeController 초대보내기: 이미 존재하는 사람");
      throw new DataAlreadyExistsException2("이미 있는 사람");
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

  public boolean facilityAndUserAndRoleExists(Long facilityId, Long userId, UserRole userRole) {
    List<Invitation> invitations = invitationRepository.findByFacilityAndUserAndRole(facilityId, userId, userRole.toString())
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

//  public Long editFacility(EditFacilityDTO dto){ // facility_id, name, address, tel, fm_name
//    Facility facility = facilityRepository.findById(dto.getFacility_id())
//                    .orElseThrow(() -> new FacilityException("시설을 찾을 수 없음"));
//
//    facility.edit(dto.getName(), dto.getAddress(), dto.getTel(), dto.getFm_name());
//
//    return facility.getId();
//  }
//
//  @Transactional(readOnly = true)
//  public Page<Facility> findAll(Pageable pageable) throws Exception {
//    Page<Facility> facilities = facilityRepository.findAll(pageable);
//
//    return facilities;
//  }

}
