package kr.ac.kumoh.allimi.service;

import kr.ac.kumoh.allimi.domain.Facility;
import kr.ac.kumoh.allimi.domain.NHResident;
import kr.ac.kumoh.allimi.domain.User;
import kr.ac.kumoh.allimi.domain.UserRole;
import kr.ac.kumoh.allimi.domain.func.Visit;
import kr.ac.kumoh.allimi.domain.func.VisitState;
import kr.ac.kumoh.allimi.dto.visit.VisitApprovalDTO;
import kr.ac.kumoh.allimi.dto.visit.VisitDeleteDTO;
import kr.ac.kumoh.allimi.dto.visit.VisitEditDTO;
import kr.ac.kumoh.allimi.dto.visit.VisitWriteDTO;
import kr.ac.kumoh.allimi.exception.FacilityException;
import kr.ac.kumoh.allimi.exception.NHResidentException;
import kr.ac.kumoh.allimi.exception.VisitException;
import kr.ac.kumoh.allimi.exception.user.UserException;
import kr.ac.kumoh.allimi.repository.FacilityRepository;
import kr.ac.kumoh.allimi.repository.NHResidentRepository;
import kr.ac.kumoh.allimi.repository.UserRepository;
import kr.ac.kumoh.allimi.repository.VisitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class VisitService {
    private final VisitRepository visitRepository;
    private final UserRepository userRepository;
    private final FacilityRepository facilityRepository;
    private final NHResidentRepository nhResidentRepository;

    public void write(VisitWriteDTO writeDTO) {
        User user = userRepository.findUserByUserId(writeDTO.getUser_id())
                .orElseThrow(() -> new UserException("사용자를 찾을 수 없습니다."));

        Facility facility = facilityRepository.findById(writeDTO.getFacility_id())
                .orElseThrow(() -> new FacilityException("시설을 찾을 수 없습니다."));

        NHResident nhResident = nhResidentRepository.findById(writeDTO.getNhresident_id())
                .orElseThrow(() -> new NHResidentException("입소자를 찾을 수 없습니다."));

        UserRole userRole = userRepository.getUserRole(writeDTO.getUser_id())
                .orElseThrow(() -> new UserException("권한을 찾을 수 없습니다."));

        if (userRole != UserRole.PROTECTOR)
            throw new UserException("보호자 외에는 면회를 신청할 수 없습니다.");

        System.out.println("visit 넣기");
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

        UserRole userRole = userRepository.getUserRole(editDTO.getUser_id())
                .orElseThrow(() -> new UserException("권한을 찾을 수 없습니다."));

        User user = visit.getUser();

        if (user.getUserId() != editDTO.getUser_id() || userRole != UserRole.PROTECTOR)
            throw new UserException("면회를 신청할 권한이 없습니다.");

        visit.editVisit(editDTO.getTexts(), editDTO.getDateTime());
    }

    public Long delete(Long visitId) {
        Visit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new VisitException("해당 면회 신청이 존재하지 않습니다."));

        Long deleted = visitRepository.deleteVisitById(visit.getId());
        return deleted;
    }

    public void approval(VisitApprovalDTO approvalDTO) {
        Visit visit = visitRepository.findById(approvalDTO.getVisit_id())
                .orElseThrow(() -> new VisitException("해당 면회 신청이 존재하지 않습니다."));

        visit.approvalVisit(approvalDTO.getState(), approvalDTO.getRejReason());
    }
}
