package kr.ac.kumoh.allimi.service;

import kr.ac.kumoh.allimi.controller.response.VisitResponse;
import kr.ac.kumoh.allimi.domain.*;
import kr.ac.kumoh.allimi.domain.func.Notice;
import kr.ac.kumoh.allimi.domain.func.Visit;
import kr.ac.kumoh.allimi.domain.func.VisitState;
import kr.ac.kumoh.allimi.dto.notice.NoticeListDTO;
import kr.ac.kumoh.allimi.dto.visit.*;
import kr.ac.kumoh.allimi.exception.FacilityException;
import kr.ac.kumoh.allimi.exception.InternalException;
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
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class VisitService {
    private final VisitRepository visitRepository;
    private final UserRepository userRepository;
    private final FacilityRepository facilityRepository;
    private final NHResidentRepository nhResidentRepository;

    //면회신청 목록보기
    public List<VisitListDTO> visitList(Long residentId) throws Exception {

        NHResident nhResident = nhResidentRepository.findById(residentId)
                .orElseThrow(() -> new NHResidentException("입소자 찾기 실패 - resident_id에 해당하는 입소자 없음"));

        UserRole userRole = nhResident.getUserRole();

        List<VisitListDTO> visitList = new ArrayList<>();

        if (userRole == UserRole.MANAGER || userRole == UserRole.WORKER) { // 직원, 시설장인 경우: 시설 면회 모두 확인 가능
            Facility facility = nhResident.getFacility();

            List<NHResident> nhResidents = nhResidentRepository.findByFacilityId(facility.getId())
                    .orElseThrow(() -> new NHResidentException("입소자 찾기 실패 - 시설에 해당하는 입소자 찾기 실패"));

            List<Visit> managerVisitList = new ArrayList<>();
            for (NHResident resident : nhResidents) {
                managerVisitList.addAll(visitRepository.findAllByProtector(resident).orElse(new ArrayList<>()));
            }
            managerVisitList = managerVisitList.stream().sorted(Comparator.comparing(Visit::getCreatedDate).reversed()).collect(Collectors.toList());
            visitList = parseVisitList(managerVisitList);
        } else if (userRole == UserRole.PROTECTOR) { // 보호자인 경우: 개별 면회만 확인 가능
            List<Visit> userNoticeList = visitRepository.findAllByProtector(nhResident)
                    .orElse(new ArrayList<>());
            visitList = parseVisitList(userNoticeList);
        } else {
            throw new NHResidentException("잘못된 역할");
        }

        return visitList;
    }

    public List<VisitListDTO> parseVisitList(List<Visit> visits) {
        List<VisitListDTO> visitList = new ArrayList<>();

        for (Visit v : visits) {
            NHResident nhResident = v.getProtector();

            NHResident protector = nhResidentRepository.findById(nhResident.getId())
                    .orElseThrow(() -> new NHResidentException("입소자 찾기 실패 - resident_id에 해당하는 입소자 없음"));
            User user = userRepository.findUserByUserId(protector.getUser().getUserId())
                    .orElseThrow(() -> new UserException("사용자 찾기 실패 - user_id에 해당하는 사용자 없음"));

            VisitListDTO dto = VisitListDTO.builder()
                    .visit_id(v.getId())
                    .protector_id(protector.getId())
                    .create_date(v.getCreatedDate())
                    .want_date(v.getWantDate())
                    .texts(v.getTexts())
                    .residentName(protector.getName())
                    .visitorName(user.getName())
                    .rejReason(v.getRejReason())
                    .state(v.getState())
                    .build();
          visitList.add(dto);
        }

        return visitList;
    }

    // 면회 신청
    public void write(VisitWriteDTO writeDTO) throws Exception {   // protector_id, dateTime, texts;
        NHResident nhResident = nhResidentRepository.findById(writeDTO.getProtector_id())
                .orElseThrow(() -> new NHResidentException("입소자 찾기 실패 - protector_id에 해당하는 입소자 없음"));
        if (nhResident.getUserRole() != UserRole.PROTECTOR)
            throw new UserAuthException("면회 신청 실패 - 권한이 없는 사용자");

        Visit visit = Visit.newVisit(nhResident, writeDTO.getTexts(), writeDTO.getDateTime());

        Visit savedVisit = visitRepository.save(visit);
        if (savedVisit == null)
            throw new InternalException("면회 신청 실패");

    }

    // 면회 수정
    public void edit(VisitEditDTO editDTO) throws Exception {  // visit_id, protector_id, dateTime, texts;
        Visit visit = visitRepository.findById(editDTO.getVisit_id())
                .orElseThrow(() -> new VisitException("면회 찾기 실패 - 해당 면회가 존재하지 않음"));

        NHResident writer = visit.getProtector();
        NHResident editer = nhResidentRepository.findById(editDTO.getProtector_id())
                .orElseThrow(() -> new NHResidentException("입소자 찾기 실패 - protector_id에 해당하는 입소자 없음"));

        if (writer.getId() != editer.getId() || editer.getUserRole() != UserRole.PROTECTOR)
            throw new UserAuthException("면회 수정 실패 - 권한이 없는 사용자");

        visit.editVisit(editDTO.getTexts(), editDTO.getDateTime());
    }

    // 면회 삭제
    public void delete(Long visitId) throws Exception {
        Visit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new VisitException("면회 찾기 실패 - 해당 면회가 존재하지 않음"));

        Long deleted = visitRepository.deleteVisitById(visit.getId());
        if (deleted == 0)
            throw new InternalException("면회 삭제 실패");
    }

    // 면회 상태 변경 - 승인, 거절, 완료
    public VisitResponse approval(VisitApprovalDTO approvalDTO) throws Exception {
        Visit visit = visitRepository.findById(approvalDTO.getVisit_id())
                .orElseThrow(() -> new VisitException("면회 찾기 실패 - 해당 면회가 존재하지 않음"));

        visit.approvalVisit(approvalDTO.getState(), approvalDTO.getRejReason());

        VisitResponse visitResponse = VisitResponse.builder()
                .state(visit.getState())
                .texts(visit.getRejReason())
                .build();

        return visitResponse;
    }
}
