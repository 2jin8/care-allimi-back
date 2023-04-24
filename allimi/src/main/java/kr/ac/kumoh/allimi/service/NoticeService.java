package kr.ac.kumoh.allimi.service;

import kr.ac.kumoh.allimi.domain.*;
import kr.ac.kumoh.allimi.dto.NoticeListDTO;
import kr.ac.kumoh.allimi.dto.NoticeResponse;
import kr.ac.kumoh.allimi.dto.NoticeWriteDto;
import kr.ac.kumoh.allimi.exception.FacilityException;
import kr.ac.kumoh.allimi.exception.NHResidentException;
import kr.ac.kumoh.allimi.exception.NoticeException;
import kr.ac.kumoh.allimi.exception.UserException;
import kr.ac.kumoh.allimi.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class NoticeService {
  private final NoticeRepository noticeRepository;
  private final UserRepository userRepository;
  private final FacilityRepository facilityRepository;
  private final NHResidentRepository nhResidentRepository;

  public void write(NoticeWriteDto dto) throws Exception {
    User user = userRepository.findUserByUserId(dto.getUser_id())
            .orElseThrow(() -> new UserException("user not found"));

    NHResident targetResident = nhResidentRepository.findById(dto.getTarget_id())
            .orElseThrow(() -> new NHResidentException("target resident not found"));

    Facility facility = facilityRepository.findById(dto.getFacility_id())
            .orElseThrow(() -> new FacilityException("facility not found"));

    Notice notice = Notice.newNotice(user, targetResident, facility, dto.getContents(), dto.getSub_contents(), dto.getImage_url());
    Notice savedNotice = noticeRepository.save(notice);

    if (savedNotice == null)
      new NoticeException("알림장 저장 실패");

  }
  //알림장 목록보기
  public List<NoticeListDTO> noticeList(Long residentId) throws Exception {
    NHResident nhResident = nhResidentRepository.findById(residentId)
            .orElseThrow(() -> new NHResidentException("nhResident를 찾을 수 없음"));

    List<Notice> notices = new ArrayList<>();
    UserRole userRole = nhResident.getUserRole();

    if (userRole == UserRole.MANAGER || userRole == UserRole.WORKER) {
      notices = managerNoticeList(nhResident);
    } else if (userRole == UserRole.PROTECTOR) {
      notices = userNoticeList(nhResident);
    } else {
      new NHResidentException("user의 역할이 잘못됨");
    }

    // Response
    List<NoticeListDTO> noticeList = new ArrayList<>();

    for (Notice notice : notices) {
      NoticeListDTO dto = NoticeListDTO.builder()
              .noticeId(notice.getId())
              .create_date(notice.getCreateDate())
              .content(notice.getContents())
              .imageUrl(notice.getImageUrl())
              .build();
      noticeList.add(dto);
    }

    return noticeList;
  }

  public List<Notice> managerNoticeList(NHResident nhResident) { // 직원, 시설장인 경우: 시설 알림장 모두 확인 가능
    Facility facility = nhResident.getFacility();
    // 시설 알림장 목록
    List<Notice> notices = noticeRepository.findAllByFacility(facility).orElse(new ArrayList<Notice>());

    return notices;
  }

  public List<Notice> userNoticeList(NHResident nhResident) {
    // 보호자인 경우: 개별 알림장만 확인 가능
    // 개별 알림장 목록
    List<Notice> notices = noticeRepository.findAllByTarget(nhResident).orElse(new ArrayList<Notice>());

    return notices;
  }

  //알림장 상세보기
  public NoticeResponse getDetail(Long noticeId) throws Exception {
    Notice notice = noticeRepository.findById(noticeId).orElseThrow(() -> new NoticeException("해당 알림장을 찾을 수 없습니다"));

    return NoticeResponse.builder()
            .create_date(notice.getCreateDate())
            .noticeId(notice.getId())
            .subContent(notice.getSubContents())
            .content(notice.getContents())
            .build();
  }

//  public void edit(NoticeEditDto editDto) throws Exception {
//    Notice notice = noticeRepository.findById(editDto.getNoticeId())
//            .orElseThrow(() -> new NoticeException("해당 notice가 없습니다"));
//
//    User writer = notice.getUser();
//
//    User user = userRepository.findUserByUserId(editDto.getUserId())
//            .orElseThrow(() -> new UserException("사용자를 찾을 수 없습니다"));
//
//    if (writer.getUserId() != editDto.getUserId() && user.getUserRole() != UserRole.MANAGER) {
//      throw new UserException("권한이 없는 사용자 입니다");
//    }
//
//    NHResident targetResident = nhResidentRepository.findById(editDto.getTargetId())
//            .orElseThrow(() -> new NHResidentException("target을 찾을 수 없습니다"));
//
//    notice.editNotice(targetResident, editDto.getContent(), editDto.getSubContent());
//  }
//
//  public Long delete(Long notice_id) {
//    Long deleted = noticeRepository.deleteNoticeById(notice_id);
//    return deleted;
//  }
}