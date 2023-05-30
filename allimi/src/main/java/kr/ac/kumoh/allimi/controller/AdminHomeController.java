package kr.ac.kumoh.allimi.controller;

import kr.ac.kumoh.allimi.domain.Facility;
import kr.ac.kumoh.allimi.dto.admin.UserListAdminDTO;
import kr.ac.kumoh.allimi.dto.facility.FacilityInfoDto;
import kr.ac.kumoh.allimi.dto.nhresident.NHResidentResponse;
import kr.ac.kumoh.allimi.exception.FacilityException;
import kr.ac.kumoh.allimi.exception.user.UserException;
import kr.ac.kumoh.allimi.service.FacilityService;
import kr.ac.kumoh.allimi.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class AdminHomeController {
  private final UserService userService;
  private final FacilityService facilityService;

  @GetMapping("/admin")
  public String home() {
    log.info("access AdminHomeController");
    return "home";
  }

  @GetMapping("/admin/users")
  public String users(Model model, @PageableDefault(sort = "userId", direction = Sort.Direction.ASC) Pageable pageable) throws Exception {
    log.info("access AdminHomeController");

    model.addAttribute("users", userService.getAllUser(pageable));
    model.addAttribute("urls", "/admin/users");
    return "userList";
  }

  @GetMapping("/admin/users/{user_id}")
  public String users(Model model, @PathVariable("user_id") Long userId) throws Exception {
    if (userId == null)
      throw new UserException("AdminHomeController 입소자 리스트 출력: user_id가 null. 사용자의 잘못된 입력");

    model.addAttribute("residents", userService.getNHResidents(userId));

    return "userDetail";
  }
  
  @GetMapping("/admin/users/search")
  public String usersSearch(String searchKeyword,
                            @PageableDefault(size = 10, sort = "userId", direction = Sort.Direction.ASC) Pageable pageable,
                            Model model) throws Exception {

    model.addAttribute("users", userService.getSearchUser(searchKeyword, pageable));
    model.addAttribute("searchKeyword", searchKeyword);
    model.addAttribute("urls", "/admin/users/search");

    return "userList";
  }

  @GetMapping("/admin/users/{user_id}/delete")
  public String usersDelete(Model model, @PathVariable("user_id") Long userId) throws Exception {
      if (userId == null)
          throw new UserException("AdminHomeController 입소자 삭제: user_id가 null.");

      userService.deleteUser(userId);

    return "redirect:/admin/users";
  }

  @GetMapping("/admin/facilities")
  public String facilities(Model model, @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable) throws Exception {
    log.info("access AdminHomeController");

    model.addAttribute("facilities", facilityService.findAllAdmin(pageable));
    model.addAttribute("urls", "/admin/facilities");

    return "facilityList";
  }

  @GetMapping("/admin/facilities/{facility_id}")
  public String facilities(Model model, @PathVariable("facility_id") Long facilityId) throws Exception {
    if (facilityId == null)
      throw new FacilityException("AdminHomeController 시설 정보 출력: facility_id가 null");

    model.addAttribute("facility", facilityService.getInfo(facilityId));

    return "facilityDetail";
  }

  @GetMapping("/admin/facilities/search")
  public String facilitiesSearch(String searchKeyword,
                            @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
                            Model model) throws Exception {

    model.addAttribute("facilities", facilityService.getSearchFacility(searchKeyword, pageable));
    model.addAttribute("searchKeyword", searchKeyword);
    model.addAttribute("urls", "/admin/facilities/search");

    return "facilityList";
  }

  @GetMapping("/admin/facilities/{facility_id}/delete")
  public String facilitiesDelete(@PathVariable("facility_id") Long facilityId) throws Exception {
      if (facilityId == null)
          throw new FacilityException("AdminHomeController 시설 삭제: facility_id가 null");

      facilityService.deleteFacility(facilityId);

      return "redirect:/admin/facilities";
  }

  @GetMapping("/bootTest")
  public String test() {
    return "bootTest";
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<String> handleDuplicate(Exception exception) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
  }
}
