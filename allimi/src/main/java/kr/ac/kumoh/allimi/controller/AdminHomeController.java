package kr.ac.kumoh.allimi.controller;

import kr.ac.kumoh.allimi.dto.admin.UserListAdminDTO;
import kr.ac.kumoh.allimi.dto.nhresident.NHResidentResponse;
import kr.ac.kumoh.allimi.exception.user.UserException;
import kr.ac.kumoh.allimi.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class AdminHomeController {
  private final UserService userService;

  @GetMapping("/admin")
  public String home() {
    log.info("access AdminHomeController");
    return "home";
  }

  @GetMapping("/admin/users")
  public String users(Model model) throws Exception {
    log.info("access AdminHomeController");

    List<UserListAdminDTO> dtos = userService.getAllUser();

    model.addAttribute("users", dtos);

    return "bootTest2";
  }

  @GetMapping("/admin/users/{user_id}")
  public String users(Model model, @PathVariable("user_id") Long userId) throws Exception {
    if (userId == null)
      throw new UserException("NHResidentController 입소자 리스트 출력: user_id가 null. 사용자의 잘못된 입력");

    List<NHResidentResponse> nhResidentResponses = userService.getNHResidents(userId);

    model.addAttribute("residents", nhResidentResponses);

    return "userDetail";
  }

//  @DeleteMapping("/admin/users")
//  public String deleteUser(Model model) throws Exception {
//    if (userId == null)
//      throw new UserException("NHResidentController 입소자 리스트 출력: user_id가 null. 사용자의 잘못된 입력");
//
//    List<NHResidentResponse> nhResidentResponses = userService.getNHResidents(userId);
//
//    model.addAttribute("residents", nhResidentResponses);
//
//    return "userDetail";
//  }

  @GetMapping("/bootTest")
  public String test() {
    return "bootTest";
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<String> handleDuplicate(Exception exception) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
  }
}
