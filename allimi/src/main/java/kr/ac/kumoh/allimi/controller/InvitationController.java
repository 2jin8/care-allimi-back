package kr.ac.kumoh.allimi.controller;

import kr.ac.kumoh.allimi.dto.facility.AddFacilityDTO;
import kr.ac.kumoh.allimi.dto.invitation.SendInvitationDto;
import kr.ac.kumoh.allimi.dto.notice.NoticeWriteDto;
import kr.ac.kumoh.allimi.exception.FacilityException;
import kr.ac.kumoh.allimi.exception.NHResidentException;
import kr.ac.kumoh.allimi.exception.user.UserAuthException;
import kr.ac.kumoh.allimi.exception.user.UserException;
import kr.ac.kumoh.allimi.service.InvitationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
@RestController
public class InvitationController {
  private final InvitationService invitationService;

  //초대보내기: facility -> user //phone_num으로 받아서 해당하는 user가 있는지 확인 후 진행
//  @PostMapping(value = "/v2/invitations")
//  public ResponseEntity sendInvitation(@RequestBody SendInvitationDto dto) { //facility_id, phone_num, userROle
//
//  }
//
//  @PostMapping(value = "/v2/notices")  // notice{user_id, nhresident_id, facility_id, contents, sub_contents}, file{}
//  public ResponseEntity noticeWrite(@RequestPart(value="notice") NoticeWriteDto dto,
//                                    @RequestPart(value="file", required = false) List<MultipartFile> files) {
//
//    if (dto.getUser_id() == null || dto.getNhresident_id() == null || dto.getFacility_id() == null) {
//      log.info("NoticeController 알림장 작성: 필요한  값이 제대로 안들어옴. 사용자의 잘못된 입력");
//      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//    }
//
//    try {
//      noticeService.write(dto, files);
//    } catch (UserAuthException e) { //알림장 쓸 권한 없음
//      log.info("NoticeController 알림장 작성: 권한이 없는 사용자");
//      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//    } catch (UserException | NHResidentException | FacilityException e) { //알림장 쓰기 실패
//      log.info("NoticeController 알림장 작성: user, resident, facility 중 하나 찾기 실패");
//      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//    } catch (Exception e) { //알림장 쓰기 실패
//      log.info("NoticeController 알림장 작성: 알림장 쓰기 실패");
//      System.out.println(e.getMessage());
//      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//    }
//
//    return ResponseEntity.status(HttpStatus.OK).build();
//  }

  //시설별 초대목록


  //초대받아주기: user -> facility
  

}
