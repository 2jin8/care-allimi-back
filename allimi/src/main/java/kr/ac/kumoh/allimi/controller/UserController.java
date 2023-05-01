package kr.ac.kumoh.allimi.controller;
import kr.ac.kumoh.allimi.controller.response.ResponseLogin;
import kr.ac.kumoh.allimi.controller.response.ResponseResidentDetail;
import kr.ac.kumoh.allimi.domain.UserRole;
import kr.ac.kumoh.allimi.dto.admin.AdminUserListDTO;
import kr.ac.kumoh.allimi.dto.user.LoginDTO;
import kr.ac.kumoh.allimi.dto.user.SignUpDTO;
import kr.ac.kumoh.allimi.dto.user.UserListDTO;
import kr.ac.kumoh.allimi.exception.user.UserAuthException;
import kr.ac.kumoh.allimi.exception.user.UserException;
import kr.ac.kumoh.allimi.exception.user.UserIdDuplicateException;
import kr.ac.kumoh.allimi.service.NHResidentService;
import lombok.RequiredArgsConstructor;
import kr.ac.kumoh.allimi.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.protocol.HTTP;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {
  private final UserService userService;
  private final NHResidentService nhResidentService;

  //전체 user조회 - 관리자용
  @GetMapping("/v2/users/admin")
  public ResponseEntity getAllUser() {
    List<AdminUserListDTO> dtos = null;
    try {
      dtos = userService.getAllUser();
    }catch (Exception exception) {
      log.info("관리자 전체 user조회: 조회 중 문제가 생김");
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    return ResponseEntity.status(HttpStatus.OK).body(dtos);
  }

  //현재 가리키는 nhresident 변경
  @PatchMapping("/v2/users/nhrs")
  public ResponseEntity changeNHResident(@RequestBody Map<String, Long> input) {
    Long userId = input.get("user_id");
    Long nhrId = input.get("nhr_id");

    if (userId == null || nhrId == null) {
      log.info("UserController 현재 user의 nhresident 변경: nhr_id 혹은 user_id가 null로 들어옴. 잘못된 요청");
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    try {
      userService.changeNHResident(userId, nhrId);
    } catch (UserException exception) {
      log.info("UserController 현재 user의 nhresident 변경: 해당하는 user가 없음");
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    } catch (Exception exception) {
      log.info("UserController 현재 user의 nhresident 변경: 변경 중 문제가 생김");
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    return ResponseEntity.status(HttpStatus.OK).build();
  }

  //user가 현재 가리키는 nhresident를 반환
  @GetMapping("/v2/users/nhrs/{user_id}")
  public ResponseEntity getUsersCurrNHR(@PathVariable("user_id") Long userId) { //user_id
    if (userId == null) {
      log.info("UserController 현재 user의 nhresident 반환: nhr_id 혹은 user_id가 null로 들어옴. 잘못된 요청");
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    ResponseResidentDetail response;

    try {
      response = userService.getCurrNHResident(userId);
    }catch (Exception exception) {
      log.info("UserController 현재 user의 nhresident 반환: 문제가 생김");
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
    
    return ResponseEntity.status(HttpStatus.OK).body(response); // nhr_id, facility_id resident_name, facility_name, user_role;
  }

  //회원가입
  @PostMapping("/v2/users")
  public ResponseEntity addUser(@RequestBody SignUpDTO dto) { // login_id, password, name, phone_num;

    if(dto.getLogin_id() == null || dto.getPassword() == null || dto.getName() == null) {
      log.info("UserController 회원가입: 필수적인 정보가 들어오지 않음");
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

      Long userId;

      try {
          userId = userService.addUser(dto);
      } catch(UserIdDuplicateException exception) { //중복된 id 에러
        log.info("회원가입: 중복된 id로 회원가입");
          return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
      } catch(Exception exception) { //그냥 에러
        log.info("회원가입: 회원가입하는데 exception이 발생");
          return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
      }

      Map<String, Long> map = new HashMap<>();
      map.put("user_id", userId);

      return ResponseEntity.status(HttpStatus.OK).body(map); // user_id
  }

  @PostMapping("/v2/login") // 로그인
  public ResponseEntity login(@RequestBody LoginDTO dto) {  // login_id, password
      ResponseLogin responseLogin;

      try {
          responseLogin = userService.login(dto.getLogin_id(), dto.getPassword());
      } catch (UserAuthException exception) {
        log.info("일치하는 id, password가 없거나 로그인 중 에러 발생");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); //해당 id password 일치하는 게 없음
      } catch (Exception exception) {
        log.info("UserController 로그인: 로그인 중 에러 발생");
          return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); //해당 id password 일치하는 게 없음
      } 

      return ResponseEntity.status(HttpStatus.OK).body(responseLogin);  //user_id, userRole
  }

  @GetMapping("/v2/users/{user_id}") // 사용자 정보 조회
  public ResponseEntity userInfo(@PathVariable("user_id") Long userId) { // user_id
      if (userId == null) {
        log.info("사용자 정보조회: user_id가 null로 들어옴. 잘못된 요청");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
      }

      UserListDTO userListDTO;

      try {
          userListDTO = userService.getUserInfo(userId);
      } catch (UserException exception) {
        log.info("사용자 정보 조회: 일치하는 user가 없음");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); //user가 없는 경우
      } catch (Exception exception) {
        log.info("사용자 정보 조회: user조회 중 에러 발생");
          return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); //user가 없는 경우
      }

      return ResponseEntity.status(HttpStatus.OK).body(userListDTO); // user_name, phone_num, login_id;
  }

  // 회원 탈퇴
  @DeleteMapping("/v1/users")
  public ResponseEntity deleteUser(@RequestBody Map<String, Long> user) { //user_id
    Long userId = user.get("user_id");

    if (userId == null) {
      log.info("회원 탈퇴: user_id가 null로 들어옴. 잘못된 요청");
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

      try {
          userService.deleteUser(userId);
      } catch (Exception exception) { //user를 찾을 수 없을 때
        log.info("회원 탈퇴: 해당하는 user를 찾을 수 없음");
          return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
      }

      return ResponseEntity.status(HttpStatus.OK).build(); //none
  }

  @PostMapping("/v2/logout") // 로그아웃 - 그냥 프론트 단에서 처리해도 될듯
  public ResponseEntity logout(@RequestBody Map<String, Long> user) { //user_id
    // TODO: 로그아웃
    return ResponseEntity.status(HttpStatus.OK).build();
  }
}


