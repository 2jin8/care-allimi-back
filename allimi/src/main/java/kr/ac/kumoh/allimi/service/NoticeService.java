package kr.ac.kumoh.allimi.service;

import kr.ac.kumoh.allimi.domain.*;
import kr.ac.kumoh.allimi.dto.NoticeEditDto;
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

  public Notice write(NoticeWriteDto dto) throws Exception {
    NoticeContent content = NoticeContent.newNoticeContent(dto.getContents(), dto.getSubContents());

    User user = userRepository.findUserByUserId(dto.getUserId())
            .orElseThrow(() -> new UserException("user not found"));

    NHResident targetResident = nhResidentRepository.findById(dto.getTarget())
            .orElseThrow(() -> new NHResidentException("target user not found"));

    Facility facility = facilityRepository.findById(dto.getFacilityId())
            .orElseThrow(() -> new FacilityException("facility not found"));

    Notice notice = Notice.newNotice(facility, user, targetResident, content);

    return noticeRepository.save(notice);
  }

  //알림장 목록보기
  public List<NoticeListDTO> noticeList(Long userId) throws Exception {
    User user = userRepository.findUserByUserId(userId).orElseThrow(() -> new UserException("user not found"));
    NHResident nhResident = user.getNhResident().get(user.getCurrentNHResident());

    List<Notice> notices;

    UserRole userRole = user.getUserRole();
    if (userRole == UserRole.MANAGER || userRole == UserRole.WORKER) {
      notices = managerNoticeList(nhResident);
    } else {
      notices = userNoticeList(nhResident);
    }

    // Response
    List<NoticeListDTO> noticeList = new ArrayList<>();

    for (Notice notice : notices) {
      NoticeContent content = notice.getContent();

      NoticeListDTO dto = NoticeListDTO.builder()
              .noticeId(content.getId())
              .create_date(content.getCreateDate())
              .content(content.getContents())
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
  public NoticeResponse findNotice(Long noticeId) throws Exception {
    Notice notice = noticeRepository.findById(noticeId).orElseThrow(() -> new NoticeException("해당 알림장을 찾을 수 없습니다"));

    NoticeContent nContent = notice.getContent();

    return NoticeResponse.builder()
            .create_date(nContent.getCreateDate())
            .noticeId(notice.getId())
            .subContent(nContent.getSubContents())
            .content(nContent.getContents())
            .build();
  }

  public void edit(NoticeEditDto editDto) throws Exception {
    Notice notice = noticeRepository.findById(editDto.getNoticeId())
            .orElseThrow(() -> new NoticeException("해당 notice가 없습니다"));

    User writer = notice.getUser();

    User user = userRepository.findUserByUserId(editDto.getUserId())
            .orElseThrow(() -> new UserException("사용자를 찾을 수 없습니다"));

    if (writer.getUserId() != editDto.getUserId() && user.getUserRole() != UserRole.MANAGER) {
      throw new UserException("권한이 없는 사용자 입니다");
    }

    NHResident targetResident = nhResidentRepository.findById(editDto.getTargetId())
            .orElseThrow(() -> new NHResidentException("target을 찾을 수 없습니다"));

    notice.editNotice(targetResident, editDto.getContent(), editDto.getSubContent());
  }

  public Long delete(Long notice_id) {
    Long deleted = noticeRepository.deleteNoticeById(notice_id);
    return deleted;
  }
}