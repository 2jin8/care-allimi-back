package kr.ac.kumoh.allimi.service;

import kr.ac.kumoh.allimi.controller.response.ResponseInvitation;
import kr.ac.kumoh.allimi.controller.response.ResponseResident;
import kr.ac.kumoh.allimi.domain.*;
import kr.ac.kumoh.allimi.dto.facility.AddFacilityDTO;
import kr.ac.kumoh.allimi.dto.facility.EditFacilityDTO;
import kr.ac.kumoh.allimi.dto.invitation.SendInvitationDto;
import kr.ac.kumoh.allimi.exception.FacilityException;
import kr.ac.kumoh.allimi.exception.InvitationException;
import kr.ac.kumoh.allimi.exception.NHResidentException;
import kr.ac.kumoh.allimi.exception.user.UserException;
import kr.ac.kumoh.allimi.repository.FacilityRepository;
import kr.ac.kumoh.allimi.repository.InvitationRepository;
import kr.ac.kumoh.allimi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class InvitationService {
  private final InvitationRepository invitationRepository;
  private final UserRepository userRepository;
  private final FacilityRepository facilityRepository;

  public Long sendInvitation(SendInvitationDto dto) throws Exception { //user_id, facility_id, userROle
    User user = userRepository.findUserByUserId(dto.getUser_id())
            .orElseThrow(() -> new UserException("해당하는 user가 없습니다"));

    Facility facility = facilityRepository.findById(dto.getFacility_id())
            .orElseThrow(() -> new FacilityException("해당하는 시설이 없습니다"));

    Invitation invitation = Invitation.makeInvitation(user, facility, UserRole.valueOf(dto.getUser_role()));
    invitationRepository.save(invitation);

    return invitation.getId();
  }

  public List<ResponseInvitation> findByFacility(Long facilityId) throws Exception {
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
              .date(i.getCreateDate())
              .build());
    }

    return list;
  }

  public void approve(Long inviteId) throws Exception{
    Invitation invitation = invitationRepository.findById(inviteId)
            .orElseThrow(() -> new InvitationException("해당 초대를 찾을 수 없음"));
    
    invitationRepository.deleteById(inviteId);
  }

  public List<ResponseInvitation> findByUser(Long userId) throws Exception {
    List<Invitation> invitations = invitationRepository.findByUserId(userId)
            .orElseGet(() -> new ArrayList<>());

    List<ResponseInvitation> list = new ArrayList<>();

    for (Invitation i : invitations) {
      User user = i.getUser();
      Facility facility = i.getFacility();

      list.add(ResponseInvitation.builder()
              .id(i.getId())
              .user_id(user.getUserId())
              .facility_id(facility.getId())
              .name(user.getName())
              .facility_name(facility.getName())
              .userRole(i.getUserRole())
              .date(i.getCreateDate())
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
//
//  @Transactional
//  public void deleteFacility(Long facility_id) throws Exception { // 회원탈퇴
//    facilityRepository.deleteById(facility_id);
//  }
}
