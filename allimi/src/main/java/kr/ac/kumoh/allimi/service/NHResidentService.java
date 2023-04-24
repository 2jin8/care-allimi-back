package kr.ac.kumoh.allimi.service;

import kr.ac.kumoh.allimi.domain.Facility;
import kr.ac.kumoh.allimi.domain.NHResident;
import kr.ac.kumoh.allimi.domain.User;
import kr.ac.kumoh.allimi.dto.NHResidentResponse;
import kr.ac.kumoh.allimi.exception.NHResidentException;
import kr.ac.kumoh.allimi.repository.NHResidentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class NHResidentService {

    @Autowired
    NHResidentRepository nhResidentRepository;

    @Transactional
    public void deleteResident(Long residentId) throws Exception { // 회원탈퇴
        //user삭제하면 resident는 null로 설정된다!
        nhResidentRepository.deleteById(residentId);
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
