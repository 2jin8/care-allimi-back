package kr.ac.kumoh.allimi.controller;

import kr.ac.kumoh.allimi.domain.User;
import kr.ac.kumoh.allimi.exception.GlobalExceptionHandler;
import kr.ac.kumoh.allimi.exception.user.UserIdDuplicateException;
import kr.ac.kumoh.allimi.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
@DisplayName("UserController 테스트")
public class UserControllerTest {
  private MockMvc mvc;

  @MockBean
  private UserService userService;

  @BeforeEach
  public void setUp() {
    mvc = MockMvcBuilders.standaloneSetup(new UserController(userService))
      .addFilters(new CharacterEncodingFilter("UTF-8", true)) // utf-8 필터 추가
      .setControllerAdvice(new GlobalExceptionHandler())
      .build();
  }

  @Test
  @DisplayName("회원가입 200ok 테스트")
  void insertUserTest() throws Exception {
    // given
    given(userService.addUser(any())).willReturn(1L);

    // when
    final ResultActions actions =
      mvc.perform(
        post("/v4/users")
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON)
          .characterEncoding("UTF-8")
          .content(
            "{"
              + "  \"login_id\" : \"id1234\","
              + "  \"password\" : \"password1234\","
              + "  \"name\" : \"주효림\","
              + "  \"phone_num\" : \"01000000000\""
              + "}"));

    // then
    actions
      .andExpect(status().isOk())
      .andExpect(jsonPath("user_id").value(1));
  }

  @Test
  @DisplayName("회원가입 필수값 안들어온거 테스트")
  void addUserInputErrorTest() throws Exception {
    // given
    given(userService.addUser(any())).willReturn(1L);

    // when
    final ResultActions actions =
      mvc.perform(
        post("/v4/users")
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON)
          .characterEncoding("UTF-8")
          .content(
            "{"
              + "  \"login_id\" : \"id1234\""
              + "}"));

    // then
    actions
      .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("회원가입 중복된 아이디 테스트")//409뜨고 conflict뜨면 정상
  void loginIdDuplicateTest() throws Exception {
    // given
    given(userService.addUser(any())).willThrow(UserIdDuplicateException.class);

    // when
    mvc.perform(
        post("/v4/users")
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON)
          .characterEncoding("UTF-8")
          .content(
            "{"
              + "  \"login_id\" : \"id1234\","
              + "  \"password\" : \"password1234\","
              + "  \"name\" : \"주효림\","
              + "  \"phone_num\" : \"01000000000\""
              + "}")
      )
      .andExpect(status().isConflict());

    // then
  }

}
