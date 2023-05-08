package kr.ac.kumoh.allimi.service;

import kr.ac.kumoh.allimi.controller.response.VisitResponse;
import kr.ac.kumoh.allimi.domain.*;
import kr.ac.kumoh.allimi.domain.func.Notice;
import kr.ac.kumoh.allimi.domain.func.Visit;
import kr.ac.kumoh.allimi.domain.func.VisitState;
import kr.ac.kumoh.allimi.dto.notice.NoticeListDTO;
import kr.ac.kumoh.allimi.dto.visit.*;
import kr.ac.kumoh.allimi.exception.FacilityException;
import kr.ac.kumoh.allimi.exception.NHResidentException;
import kr.ac.kumoh.allimi.exception.VisitException;
import kr.ac.kumoh.allimi.exception.user.UserAuthException;
import kr.ac.kumoh.allimi.exception.user.UserException;
import kr.ac.kumoh.allimi.repository.FacilityRepository;
import kr.ac.kumoh.allimi.repository.NHResidentRepository;
import kr.ac.kumoh.allimi.repository.UserRepository;
import kr.ac.kumoh.allimi.repository.VisitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class VisitService {
    private final VisitRepository visitRepository;
    private final UserRepository userRepository;
    private final FacilityRepository facilityRepository;
    private final NHResidentRepository nhResidentRepository;

    //면회신청 목록보기
    public List<VisitListDTO> visitList(Long userId) throws Exception {
      User user = userRepository.findUserByUserId(userId)
              .orElseThrow(() -> new UserException("user를 찾을 수 없음"));

      NHResident nhResident = nhResidentRepository.findById(user.getCurrentNHResident())
              .orElseThrow(()-> new NHResidentException("입소자를 찾을 수 없음"));

      UserRole userRole = userRepository.getUserRole(user.getCurrentNHResident(), user.getUserId())
              .orElseThrow(() -> new UserException("user의 역할을 찾을 수 없음"));

      List<VisitListDTO> visitList = new ArrayList<>();

      if (userRole == UserRole.MANAGER || userRole == UserRole.WORKER) { // 직원, 시설장인 경우: 시설 알림장 모두 확인 가능
        Facility facility = nhResident.getFacility();
        List<Visit> managerVisitList = visitRepository.findAllByFacility(facility)
                .orElse(new ArrayList<Visit>());
        visitList = parseVisitList(managerVisitList);
      } else if (userRole == UserRole.PROTECTOR) { // 보호자인 경우: 개별 알림장만 확인 가능
        List<Visit> userNoticeList = visitRepository.findAllByTarget(nhResident)
                .orElse(new ArrayList<Visit>());
        visitList = parseVisitList(userNoticeList);
      } else {
        throw new NHResidentException("user의 역할이 잘못됨");
      }

      return visitList;
    }

  public List<VisitListDTO> parseVisitList(List<Visit> visits) {
    List<VisitListDTO> visitList = new ArrayList<>();

    for (Visit v : visits) {
      User user = v.getUser();
      NHResident resident = v.getNhResident();

      VisitListDTO dto = VisitListDTO.builder()
              .visit_id(v.getId())
              .user_id(user.getUserId())
              .resident_id(resident.getId())
              .create_date(v.getCreateDate())
              .want_date(v.getWantDate())
              .texts(v.getTexts())
              .phoneNum(v.getPhoneNum())
              .visitorName(v.getVisitorName())
              .rejReason(v.getRejReason())
              .state(v.getState())
              .build();
      visitList.add(dto);
    }

    return visitList;
  }


  public void write(VisitWriteDTO writeDTO) {
        User user = userRepository.findUserByUserId(writeDTO.getUser_id())
                .orElseThrow(() -> new UserException("사용자를 찾을 수 없습니다."));

        Facility facility = facilityRepository.findById(writeDTO.getFacility_id())
                .orElseThrow(() -> new FacilityException("시설을 찾을 수 없습니다."));

        NHResident nhResident = nhResidentRepository.findById(writeDTO.getNhresident_id())
                .orElseThrow(() -> new NHResidentException("입소자를 찾을 수 없습니다."));

        UserRole userRole = userRepository.getUserRole(user.getCurrentNHResident(), user.getUserId())
                .orElseThrow(() -> new UserException("역할을 찾을 수 없습니다."));

        if (userRole != userRole.PROTECTOR)
            throw new UserAuthException("권한이 없는 사용자입니다.");

        Visit visit = Visit.newVisit(user, nhResident, facility, user.getName(), writeDTO.getTexts(), writeDTO.getDateTime());

        Visit savedVisit = visitRepository.save(visit);
        if (savedVisit == null)
            throw new VisitException("면회 신청이 실패했습니다.");
    }

    public void edit(VisitEditDTO editDTO) {
        Visit visit = visitRepository.findById(editDTO.getVisit_id())
                .orElseThrow(() -> new VisitException("해당 면회가 없습니다."));

        NHResident nhResident = nhResidentRepository.findById(editDTO.getNhresident_id())
                .orElseThrow(() -> new NHResidentException("입소자를 찾을 수 없습니다."));

        UserRole userRole = userRepository.getUserRole(nhResident.getId(), editDTO.getUser_id())
                .orElseThrow(() -> new UserException("역할을 찾을 수 없습니다."));

        User user = visit.getUser();

        if (user.getUserId() != editDTO.getUser_id() || userRole != UserRole.PROTECTOR) {
            throw new UserAuthException("권한이 없는 사용자입니다.");
        }

        visit.editVisit(editDTO.getTexts(), editDTO.getDateTime());
    }

    public Long delete(Long visitId) {
        Visit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new VisitException("해당 면회 신청이 존재하지 않습니다."));

        Long deleted = visitRepository.deleteVisitById(visit.getId());
        return deleted;
    }

    public VisitResponse approval(VisitApprovalDTO approvalDTO) {
        Visit visit = visitRepository.findById(approvalDTO.getVisit_id())
                .orElseThrow(() -> new VisitException("해당 면회 신청이 존재하지 않습니다."));

        visit.approvalVisit(approvalDTO.getState(), approvalDTO.getRejReason());

        VisitResponse visitResponse = VisitResponse.builder()
                .state(visit.getState())
                .texts(visit.getRejReason())
                .build();

        return visitResponse;
    }
}
