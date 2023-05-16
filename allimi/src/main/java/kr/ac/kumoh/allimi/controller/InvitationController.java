package kr.ac.kumoh.allimi.controller;

import jakarta.validation.Valid;
import kr.ac.kumoh.allimi.controller.response.ResponseInvitation;
import kr.ac.kumoh.allimi.domain.UserRole;
import kr.ac.kumoh.allimi.dto.invitation.SendInvitationDto;
import kr.ac.kumoh.allimi.exception.FacilityException;
import kr.ac.kumoh.allimi.exception.InvitationException;
import kr.ac.kumoh.allimi.exception.user.UserException;
import kr.ac.kumoh.allimi.service.InvitationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
  public ResponseEntity sendInvitation(@Valid @RequestBody SendInvitationDto dto) throws Exception { //user_id, facility_id, userRole

    //중복 초대 방지
    List<ResponseInvitation> invitations;
    boolean hasData = invitationService.findByFacilityAndUserAndRoleExists(dto.getFacility_id(),
            dto.getUser_id(), UserRole.valueOf(dto.getUser_role()));

    if (hasData) {
      log.info("NoticeController 초대보내기: 중복된 초대장");
      return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    //이미 있는 입소자 경우 초대장 보내면 안됨
    //@TODO

    //초대 보내기
    Long inviteId = invitationService.sendInvitation(dto);

    Map<String, Long> map = new HashMap<>();
    map.put("invitation_id", inviteId);

    return ResponseEntity.status(HttpStatus.OK).body(map); // user_id
  }

  //시설별 초대목록
  @GetMapping("/v2/invitations/{facility_id}")
  public ResponseEntity invitationByFacility(@PathVariable("facility_id") Long facilityId) throws Exception {
    if (facilityId == null)
      throw new FacilityException("InvitationController 시설별 초대목록: facility_id가 null");

    List<ResponseInvitation> invitations = invitationService.findByFacility(facilityId);

    return ResponseEntity.status(HttpStatus.OK).body(invitations);
  }

  //나에게 온 초대 목록
  @GetMapping("/v2/users/invitations/{user_id}")
  public ResponseEntity invitationForUser(@PathVariable("user_id") Long userId) throws Exception {
    if (userId == null)
      throw new UserException("InvitationController 나에게 온 초대 목록: user_id가 null");

    List<ResponseInvitation> invitations = invitationService.findByUser(userId);

    return ResponseEntity.status(HttpStatus.OK).body(invitations); // id, user_id, facility_id, name, facility_name, userRole, date;
  }

  //초대받아주기: user -> facility
  @PostMapping("/v2/invitations/approve")
  public ResponseEntity approveInvitation(@RequestBody Map<String, Long> invite) throws Exception { //invite_id
    Long inviteId = invite.get("invite_id");
    if (inviteId == null)
      throw new InvitationException("InvitationController 초대 받아주기: invite_id가 null");

    invitationService.approve(inviteId);

    return ResponseEntity.status(HttpStatus.OK).build(); // user_id
  }

  //초대 삭제
  @DeleteMapping("/v2/invitations")
  public ResponseEntity invitationDelete(@RequestBody Map<String, Long> invitation) {

    Long invitationId = invitation.get("invit_id");
    if (invitation == null)
      throw new InvitationException("InvitationController 초대 삭제: invite_id가 null");

    int deletedCnt = invitationService.delete(invitationId);

    if (deletedCnt == 0)
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

    return ResponseEntity.status(HttpStatus.OK).build();
  }
}
