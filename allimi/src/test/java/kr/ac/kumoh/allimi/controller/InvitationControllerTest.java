package kr.ac.kumoh.allimi.controller;

import kr.ac.kumoh.allimi.controller.response.ResponseInvitation;
import kr.ac.kumoh.allimi.dto.facility.AddFacilityDTO;
import kr.ac.kumoh.allimi.dto.invitation.SendInvitationDto;
import kr.ac.kumoh.allimi.dto.user.UserDTO;
import kr.ac.kumoh.allimi.service.FacilityService;
import kr.ac.kumoh.allimi.service.InvitationService;
import kr.ac.kumoh.allimi.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest //스프링부트 application 테스트 실행할 때 필요한 대부분의 의존성 제공
@Slf4j
class InvitationControllerTest {
  @Autowired
  InvitationService invitationService;
  @Autowired
  UserService userService;
  @Autowired
  FacilityService facilityService;

  @Test
  @Transactional
  @DisplayName("초대장 보내기 테스트")
  @Rollback(true)
  public void sendInvitation() {
    log.info("@@초대장 보내기 테스트");

    UserDTO.SignUp signupDto = UserDTO.SignUp.builder()
            .login_id("invitationTestId")
            .password("1234")
            .name("쿠로미")
            .phone_num("010000000000")
            .build();

    //user하나 만들기
    Long userId = 0L;
    try {
      userId = userService.addUser(signupDto);
    } catch(Exception e) {
      log.info("user 추가가 이상함");
      assertThat("오류나면 안됨").isEqualTo(" ");
    }

    //facility 하나 만들기
    AddFacilityDTO facilityDto = AddFacilityDTO.builder()
            .name("초대테스트요양원")
            .address("금오산")
            .tel("01000000000")
            .fm_name("금오장")
            .build();

    Long facilityId = 0L;
    try {
      facilityId = facilityService.addFacility(facilityDto);
    } catch(Exception exception) { //그냥 에러
      log.info("시설 추가가 이상함");
      assertThat("오류나면 안됨").isEqualTo(" ");
    }

    SendInvitationDto dto = SendInvitationDto.builder()
            .user_id(userId)
            .facility_id(facilityId)
            .user_role("PROTECTOR")
            .build();

    Long inviteId = 0L;

    try {
      inviteId = invitationService.sendInvitation(dto);
    } catch (Exception e) {
      log.info("초대장 보내기가 이상함");
      assertThat("오류나면 안됨").isEqualTo(" ");
    }

    List<ResponseInvitation> invitations = new ArrayList<ResponseInvitation>();

    try {
      invitations = invitationService.findByUser(userId);
    } catch (Exception exception) {
      log.info("초대장 받아오기가 이상함");
      assertThat("오류나면 안됨").isEqualTo(" ");
    }

    boolean findItem = false;
    for (ResponseInvitation response: invitations) {
      if (response.getId() == inviteId) {
        findItem = true;
        break;
      }
    }

    assertThat(findItem).isEqualTo(true);
  }

  @Test
  @Transactional
  @DisplayName("초대 승인 테스트")
  @Rollback(true)
  public void approveInvitation() {
    log.info("@@초대장 보내기 테스트");

    UserDTO.SignUp signupDto = UserDTO.SignUp.builder()
            .login_id("invitationTestId")
            .password("1234")
            .name("쿠로미")
            .phone_num("010000000000")
            .build();

    //user하나 만들기
    Long userId = 0L;
    try {
      userId = userService.addUser(signupDto);
    } catch(Exception e) {
      log.info("user 추가가 이상함");
      assertThat("오류나면 안됨").isEqualTo(" ");
    }

    //facility 하나 만들기
    AddFacilityDTO facilityDto = AddFacilityDTO.builder()
            .name("초대테스트요양원")
            .address("금오산")
            .tel("01000000000")
            .fm_name("금오장")
            .build();

    Long facilityId = 0L;
    try {
      facilityId = facilityService.addFacility(facilityDto);
    } catch(Exception exception) { //그냥 에러
      log.info("시설 추가가 이상함");
      assertThat("오류나면 안됨").isEqualTo(" ");
    }

    SendInvitationDto dto = SendInvitationDto.builder()
            .user_id(userId)
            .facility_id(facilityId)
            .user_role("PROTECTOR")
            .build();

    Long inviteId = 0L;

    try {
      inviteId = invitationService.sendInvitation(dto);
    } catch (Exception e) {
      log.info("초대장 보내기가 이상함");
      assertThat("오류나면 안됨").isEqualTo(" ");
    }

    try {
      invitationService.approve(inviteId);
    } catch (Exception e) {
      log.info("초대장 승인이 이상함");
      assertThat("오류나면 안됨").isEqualTo(" ");
    }

    List<ResponseInvitation> invitations = new ArrayList<ResponseInvitation>();

    //승인됐는지 확인 - 삭제됐는지 ㅇㅇ
    try {
      invitations = invitationService.findByUser(userId);
    } catch (Exception exception) {
      log.info("초대장 받아오기가 이상함");
      assertThat("오류나면 안됨").isEqualTo(" ");
    }

    boolean findItem = false;
    for (ResponseInvitation response: invitations) {
      if (response.getId() == inviteId) {
        findItem = true;
        break;
      }
    }

    assertThat(findItem).isEqualTo(false);
  }
}