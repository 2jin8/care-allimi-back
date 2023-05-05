package kr.ac.kumoh.allimi.controller;

import kr.ac.kumoh.allimi.controller.response.ResponseInvitation;
import kr.ac.kumoh.allimi.domain.UserRole;
import kr.ac.kumoh.allimi.dto.invitation.SendInvitationDto;
import kr.ac.kumoh.allimi.service.InvitationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
@RestController
public class InvitationController {
  private final InvitationService invitationService;

  //초대보내기: facility -> user //phone_num으로 받아서 해당하는 user가 있는지 확인 후 진행
  @PostMapping(value = "/v2/invitations")
  public ResponseEntity sendInvitation(@RequestBody SendInvitationDto dto) { //user_id, facility_id, userRole
    if (dto.getUser_id() == null || dto.getFacility_id() == null) {
      log.info("NoticeController 초대보내기: 필요한  값이 제대로 안들어옴. 사용자의 잘못된 입력");
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    Long inviteId;

    try {
      inviteId = invitationService.sendInvitation(dto);
    } catch (Exception e) {
      log.info("NoticeController 초대 보내기: 실패" + e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    Map<String, Long> map = new HashMap<>();
    map.put("invitation_id", inviteId);

    return ResponseEntity.status(HttpStatus.OK).body(map); // user_id
  }

  //시설별 초대목록
  @GetMapping("/v2/invitations/{facility_id}")
  public ResponseEntity invitationByFacility(@PathVariable("facility_id") Long facilityId) {
    if (facilityId == null) {
      log.info("NHResidentController 모든 입소자 출력 - 관리자용: nhresident_id값이 제대로 안들어옴. 사용자의 잘못된 입력");
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    List<ResponseInvitation> invitations;

    try {
      invitations = invitationService.findByFacility(facilityId);
    } catch (Exception exception) {
      log.info("InvitationController 시설별 초대장 출력 - 관리자용: nhresident 리스트 찾기 실패");
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    return ResponseEntity.status(HttpStatus.OK).body(invitations);
  }

  //나에게 온 초대 목록
  @GetMapping("/v2/users/invitations/{user_id}")
  public ResponseEntity invitationForUser(@PathVariable("user_id") Long userId) {
    if (userId == null) {
      log.info("InvitationController 나에게 온 초대장 출력 - 관리자용: nhresident_id값이 제대로 안들어옴. 사용자의 잘못된 입력");
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    List<ResponseInvitation> invitations;

    try {
      invitations = invitationService.findByUser(userId);
    } catch (Exception exception) {
      log.info("InvitationController 나에게 온 초대장 출력 - 관리자용: nhresident 리스트 찾기 실패");
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    return ResponseEntity.status(HttpStatus.OK).body(invitations); // id, user_id, facility_id, name, facility_name, userRole, date;
  }

   //초대받아주기: user -> facility
  @PostMapping("/v2/invitations/approve")
  public ResponseEntity approveInvitation(@RequestBody Map<String, Long> invite) { //invite_id
    Long inviteId = invite.get("invite_id");

    if (inviteId == null) {
      log.info("InvitationController 초대받아주기: 필요한  값이 제대로 안들어옴. 사용자의 잘못된 입력");
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    try {
      invitationService.approve(inviteId);
    } catch (Exception e) {
      log.info("InvitationController 초대받아주기: 실패" + e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
    return ResponseEntity.status(HttpStatus.OK).build(); // user_id
  }

}
