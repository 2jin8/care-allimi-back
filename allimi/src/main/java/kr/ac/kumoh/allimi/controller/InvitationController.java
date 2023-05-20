package kr.ac.kumoh.allimi.controller;

import jakarta.validation.Valid;
import kr.ac.kumoh.allimi.controller.response.ResponseInvitation;
import kr.ac.kumoh.allimi.domain.UserRole;
import kr.ac.kumoh.allimi.dto.invitation.SendInvitationDto;
import kr.ac.kumoh.allimi.exception.FacilityException;
import kr.ac.kumoh.allimi.exception.InputException;
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
@RequestMapping("/v4")
@Slf4j
@RestController
public class InvitationController {
  private final InvitationService invitationService;

  //초대보내기: facility -> user //phone_num으로 받아서 해당하는 user가 있는지 확인 후 진행 - 이미 있는 초대장 409 / 이미 있는 입소자 406
  @PostMapping(value = "/invitations")
  public ResponseEntity sendInvitation(@Valid @RequestBody SendInvitationDto dto) throws Exception { //user_id, facility_id, userRole
    Long inviteId = invitationService.sendInvitation(dto);

    Map<String, Long> map = new HashMap<>();
    map.put("invitation_id", inviteId);

    return ResponseEntity.status(HttpStatus.OK).body(map); // user_id
  }

  //시설별 초대목록
  @GetMapping("/invitations/{facility_id}")
  public ResponseEntity invitationByFacility(@PathVariable("facility_id") Long facilityId) throws Exception {
    List<ResponseInvitation> invitations = invitationService.findByFacility(facilityId);

    return ResponseEntity.status(HttpStatus.OK).body(invitations);
  }

  //나에게 온 초대 목록
  @GetMapping("/users/invitations/{user_id}")
  public ResponseEntity invitationForUser(@PathVariable("user_id") Long userId) throws Exception {
    List<ResponseInvitation> invitations = invitationService.findByUser(userId);

    return ResponseEntity.status(HttpStatus.OK).body(invitations); // id, user_id, facility_id, name, facility_name, userRole, date;
  }

  //초대받아주기: user -> facility
  @PostMapping("/invitations/approve")
  public ResponseEntity approveInvitation(@RequestBody Map<String, Long> invite) throws Exception { //invite_id
    Long inviteId = invite.get("invite_id");
    if (inviteId == null)
      throw new InputException("InvitationController 초대 받아주기: invite_id가 null");

    invitationService.approve(inviteId);

    return ResponseEntity.status(HttpStatus.OK).build(); // user_id
  }

  //초대 삭제
  @DeleteMapping("/invitations")
  public ResponseEntity invitationDelete(@RequestBody Map<String, Long> invitation) {
    Long invitationId = invitation.get("invit_id");
    if (invitation == null)
      throw new InputException("InvitationController 초대 삭제: invite_id가 null: 사용자의 잘못된 요청");

    int deletedCnt = invitationService.delete(invitationId);

    if (deletedCnt == 0)
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

    return ResponseEntity.status(HttpStatus.OK).build();
  }
}
