package kr.ac.kumoh.allimi.service;

import kr.ac.kumoh.allimi.domain.NHResident;
import kr.ac.kumoh.allimi.domain.User;
import kr.ac.kumoh.allimi.domain.UserRole;
import kr.ac.kumoh.allimi.dto.nhresident.NHResidentResponse;
import kr.ac.kumoh.allimi.exception.NHResidentException;
import kr.ac.kumoh.allimi.exception.user.UserAuthException;
import kr.ac.kumoh.allimi.exception.user.UserException;
import kr.ac.kumoh.allimi.repository.NHResidentRepository;
import kr.ac.kumoh.allimi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NHResidentService {
    private final NHResidentRepository nhResidentRepository;
    private final UserRepository userRepository;

    @Transactional
    public void deleteResident(Long residentId) throws Exception { // 회원탈퇴
        //user삭제하면 resident는 null로 설정된다!
        nhResidentRepository.deleteById(residentId);
    }

//    nhResidentService.approve(userId, residentId);

    @Transactional
    public void approve(Long approverId, Long residentId) throws Exception {
        NHResident approver = nhResidentRepository.findById(approverId)
                .orElseThrow(() -> new NHResidentException("승인자를 찾을 수 없습니다"));

        if (approver.getUserRole() != UserRole.MANAGER && approver.getUserRole() != UserRole.WORKER)
            new UserAuthException("권한이 없는 사용자입니다");

        NHResident resident = nhResidentRepository.findById(residentId)
                .orElseThrow(() -> new NHResidentException("입소자를 찾을 수 없습니다"));

        resident.approve();
    }

    @Transactional(readOnly = true)
    public List<NHResidentResponse> notApprovedList(Long facilityId) throws Exception {
        List<NHResident> list = nhResidentRepository.findNotApproved(facilityId)
                .orElseGet(() -> new ArrayList<>());

        List<NHResidentResponse> responseList = new ArrayList<>();

        for (NHResident nhr: list) {
            NHResidentResponse response = NHResidentResponse.builder()
                    .resident_id(nhr.getId())
                    .resident_name(nhr.getName())
                    .user_role(nhr.getUserRole())
                    .is_approved(null)
                    .build();

            responseList.add(response);
        }

        return responseList;
    }

//    @Transactional(readOnly = true)
//    public List<NHResidentResponse> getList(Long userId) throws Exception {
//
//        List<NHResident> list = nhResidentRepository.findByUserId(userId).orElseThrow(() ->
//                new NHResidentException("불러오기 실패"));
//
//        List<NHResidentResponse> nhResidentResponses = new ArrayList<>();
//
//        for (NHResident nhr: list) {
//            User user = nhr.getUser();
//            Facility facility = nhr.getFacility();
//
//            nhResidentResponses.add(NHResidentResponse.builder()
//                    .user_id(user.getUserId())
//                    .facility_name(facility.getName())
//                    .name(nhr.getName())
//                    .userRole(nhr.getUserRole())
//                    .build());
//        }
//
//        return nhResidentResponses;
//    }
}
