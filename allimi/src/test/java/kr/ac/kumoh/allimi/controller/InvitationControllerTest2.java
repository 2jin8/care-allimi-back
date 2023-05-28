package kr.ac.kumoh.allimi.controller;

import kr.ac.kumoh.allimi.controller.response.ResponseInvitation;
import kr.ac.kumoh.allimi.dto.facility.AddFacilityDTO;
import kr.ac.kumoh.allimi.dto.invitation.SendInvitationDto;
import kr.ac.kumoh.allimi.exception.GlobalExceptionHandler;
import kr.ac.kumoh.allimi.service.FacilityService;
import kr.ac.kumoh.allimi.service.InvitationService;
import kr.ac.kumoh.allimi.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@WebMvcTest(InvitationController.class)
@ExtendWith(SpringExtension.class)
@DisplayName("InvitationController 테스트")
class InvitationControllerTest2 {
  private MockMvc mvc;

  @MockBean
  private InvitationService invitationService;

  @BeforeEach
  public void setUp() {
    mvc = MockMvcBuilders.standaloneSetup(new InvitationController(invitationService))
      .addFilters(new CharacterEncodingFilter("UTF-8", true)) // utf-8 필터 추가
      .setControllerAdvice(new GlobalExceptionHandler())
      .build();
  }

  @Test
  @DisplayName("초대장 보내기 200ok 테스트")
  public void sendInvitation() throws Exception {
    //given
    given(invitationService.sendInvitation(any())).willReturn(1L);

    //when
    final ResultActions actions =
      mvc.perform(
        post("/v4/invitations")
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON)
          .characterEncoding("UTF-8")
          .content(
            "{"
              + "  \"user_id\" : \"1\","
              + "  \"facility_id\" : \"1\","
              + "  \"user_role\" : \"PROTECTOR\""
              + "}"
          )
      );

    //then
    actions
      .andExpect(status().isOk())
      .andExpect(jsonPath("invitation_id").value(1));
  }

  @Test
  public void sendInvitInputErrorTest() throws Exception {
    // given
    given(invitationService.sendInvitation(any())).willReturn(1L);

    //when
    final ResultActions actions =
      mvc.perform(
        post("/v4/invitations")
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON)
          .characterEncoding("UTF-8")
          .content(
            "{"
              + "  \"user_id\" : \"1\","
              + "  \"user_role\" : \"PROTECTOR\""
              + "}"
          )
      );

    // then
    actions
      .andExpect(status().isBadRequest());
  }

  //NoSuchElementException
  @Test
  public void sendInvitDataNotFound() throws Exception {
    // given
    given(invitationService.sendInvitation(any())).willThrow(NoSuchElementException.class);

    // when
    final ResultActions actions = mvc.perform(
        post("/v4/invitations")
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON)
          .characterEncoding("UTF-8")
          .content(
            "{"
              + "  \"user_id\" : \"1\","
              + "  \"facility_id\" : \"1\","
              + "  \"user_role\" : \"PROTECTOR\""
              + "}"
          )
      );

    // then
    actions.andExpect(status().isNotFound());
  }

  //이미 있는 초대장 DataAlreadyExistsException
  //TODO service testcode가 먼저인 것 같아서 미룸


  //이미 존재하는 사람 DataAlreadyExistsException2

}




















