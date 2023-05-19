package kr.ac.kumoh.allimi.controller;
import jakarta.validation.Valid;
import kr.ac.kumoh.allimi.controller.response.ResponseLogin;
import kr.ac.kumoh.allimi.controller.response.ResponseResidentDetail;
import kr.ac.kumoh.allimi.dto.admin.UserListAdminDTO;
import kr.ac.kumoh.allimi.dto.user.LoginDTO;
import kr.ac.kumoh.allimi.dto.user.SignUpDTO;
import kr.ac.kumoh.allimi.dto.ids.UserNHResidentDTO;
import kr.ac.kumoh.allimi.dto.user.UserEditDTO;
import kr.ac.kumoh.allimi.dto.user.UserListDTO;
import kr.ac.kumoh.allimi.exception.InputException;
import kr.ac.kumoh.allimi.exception.user.UserException;
import lombok.RequiredArgsConstructor;
import kr.ac.kumoh.allimi.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class UserController {
  private final UserService userService;

  //회원가입
  @PostMapping("/v2/users")
  public ResponseEntity addUser(@RequestBody @Valid SignUpDTO dto) throws Exception { // login_id, password, name, phone_num;
    Long userId = userService.addUser(dto);

    Map<String, Long> map = new HashMap<>();
    map.put("user_id", userId);

    return ResponseEntity.status(HttpStatus.OK).body(map); // user_id
  }

  @PostMapping("/v2/login") // 로그인
  public ResponseEntity login(@RequestBody @Valid LoginDTO dto) throws Exception {  // login_id, password
    ResponseLogin responseLogin = userService.login(dto.getLogin_id(), dto.getPassword());

    return ResponseEntity.status(HttpStatus.OK).body(responseLogin);  // user_id, user_role, user_name, phone_num, login_id;
  }

  //전체 user조회 - 관리자용
  @GetMapping("/v2/users")
  public ResponseEntity getAllUser() throws Exception {
    List<UserListAdminDTO> dtos = userService.getAllUser();

    return ResponseEntity.status(HttpStatus.OK).body(dtos);
  }

  //회원 탈퇴
  @DeleteMapping("/v2/users")
  public ResponseEntity deleteUser(@RequestBody @NotNull Map<String, Long> user) throws Exception { //user_id // TODO: 오류 수정
    Long userId = user.get("user_id");
    userService.deleteUser(userId);

    return ResponseEntity.status(HttpStatus.OK).build(); //none
  }

  @PatchMapping("/v2/users")
  public ResponseEntity editUser(@RequestBody @Valid UserEditDTO dto) throws Exception {
    userService.edit(dto);

    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @GetMapping("/v2/users/{user_id}") // 사용자 정보 조회
  public ResponseEntity userInfo(@PathVariable("user_id") Long userId) throws Exception { // user_id
    if (userId == null)
      throw new InputException("UserController 사용자 정보조회: user_id가 null로 들어옴. 잘못된 요청");

    UserListDTO userListDTO = userService.getUserInfo(userId);

    return ResponseEntity.status(HttpStatus.OK).body(userListDTO); // user_name, phone_num, login_id;
  }

  //현재 가리키는 nhresident 변경
  @PatchMapping("/v2/users/nhrs")
  public ResponseEntity changeNHResident(@Valid @RequestBody UserNHResidentDTO userNHResidentDTO) throws Exception {
    userService.changeNHResident(userNHResidentDTO.getUser_id(), userNHResidentDTO.getNhr_id());

    return ResponseEntity.status(HttpStatus.OK).build();
  }

  //user가 현재 가리키는 nhresident를 반환
  @GetMapping("/v2/users/nhrs/{user_id}")
  public ResponseEntity getUsersCurrNHR(@PathVariable(value = "user_id") Long userId) throws Exception { //user_id
    ResponseResidentDetail response = userService.getCurrNHResident(userId);

    return ResponseEntity.ok().body(response); // nhr_id, facility_id resident_name, facility_name, user_role;
  }

  //전화번호 맞는 user 모두 출력
  @GetMapping(value = "/v2/users/phone-num/{phone_num}")
  public ResponseEntity getUserByPhoneNum(@PathVariable("phone_num") String phoneNum) throws Exception {
    List<UserListAdminDTO> dtos = userService.getUserByPhoneNum(phoneNum);

    return ResponseEntity.status(HttpStatus.OK).body(dtos);
  }


  @PostMapping("/v2/logout") // 로그아웃 - 그냥 프론트 단에서 처리해도 될듯
  public ResponseEntity logout(@RequestBody Map<String, Long> user) { //user_id
    // TODO: 로그아웃
    return ResponseEntity.status(HttpStatus.OK).build();
  }

}


