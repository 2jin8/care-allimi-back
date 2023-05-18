package kr.ac.kumoh.allimi.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class AdminHomeController {

  @GetMapping("/admin")
  public String home() {
    log.info("access AdminHomeController");
    return "home";
  }

  @GetMapping("/bootTest")
  public String test() {
    return "bootTest";
  }
}
