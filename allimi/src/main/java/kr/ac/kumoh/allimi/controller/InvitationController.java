package kr.ac.kumoh.allimi.controller;

import kr.ac.kumoh.allimi.service.InvitationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
@RestController
public class InvitationController {
  private final InvitationService invitationService;

  //초대보내기: facility -> user //phone_num으로 받아서 해당하는 user가 있는지 확인 후 진행

  //시설별 초대목록

  //초대받아주기: user -> facility
  

}
