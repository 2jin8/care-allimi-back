package kr.ac.kumoh.allimi.service;

import kr.ac.kumoh.allimi.controller.response.ResponseResident;
import kr.ac.kumoh.allimi.domain.Facility;
import kr.ac.kumoh.allimi.domain.NHResident;
import kr.ac.kumoh.allimi.domain.User;
import kr.ac.kumoh.allimi.domain.UserRole;
import kr.ac.kumoh.allimi.dto.nhresident.NHResidentDTO;
import kr.ac.kumoh.allimi.dto.nhresident.NHResidentEditDTO;
import kr.ac.kumoh.allimi.dto.nhresident.NHResidentResponse;
import kr.ac.kumoh.allimi.exception.FacilityException;
import kr.ac.kumoh.allimi.exception.NHResidentException;
import kr.ac.kumoh.allimi.exception.user.UserAuthException;
import kr.ac.kumoh.allimi.exception.user.UserException;
import kr.ac.kumoh.allimi.repository.FacilityRepository;
import kr.ac.kumoh.allimi.repository.InvitationRepository;
import kr.ac.kumoh.allimi.repository.NHResidentRepository;
import kr.ac.kumoh.allimi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NHResidentService {
    private final NHResidentRepository nhResidentRepository;
    private final UserRepository userRepository;
    private final FacilityRepository facilityRepository;
    private final InvitationRepository invitationRepository;

    @Transactional
    public Long addNHResident(NHResidentDTO dto) throws Exception { // user_id, facility_id, resident_name, birth, user_role, health_info;
        User user = userRepository.findUserByUserId(dto.getUser_id())
                .orElseThrow(() -> new UserException("해당 user가 없습니다"));

        Facility facility = facilityRepository.findById(dto.getFacility_id())
                .orElseThrow(() -> new FacilityException("시설을 찾을 수 없습니다"));

        NHResident nhResident = NHResident.newNHResident(user, facility, dto.getResident_name(), dto.getUser_role(), dto.getBirth(), dto.getHealth_info());
        nhResidentRepository.save(nhResident);

        user.changeCurrNHResident(nhResident.getId());

        return nhResident.getId();
    }

    @Transactional
    public void deleteResident(Long residentId) throws Exception { // 입소자 삭제
//      NHResident resident = nhResidentRepository.findById(residentId)
//              .orElseThrow(() -> new NHResidentException("해당하는 입소자가 없음"));
//      User user = resident.getUser();

      //user삭제하면 resident는 null로 설정된다!
      nhResidentRepository.deleteById(residentId);
//      List<NHResidentResponse> nhResidentResponses;
//      userRepository.
    }

  @Transactional
  public void editNHResident(NHResidentEditDTO dto) throws Exception { //resident_id, resident_name, birth, health_info
    NHResident nhResident = nhResidentRepository.findById(dto.getResident_id())
                    .orElseThrow(() -> new NHResidentException("해당하는 resident가 없음"));

    nhResident.edit(dto.getResident_name(), dto.getBirth(), dto.getHealth_info());
  }

  public List<ResponseResident> findProtectorByFacility(Long facilityId) throws Exception {
    List<NHResident> residents = nhResidentRepository.findProtectorByFacilityId(facilityId)
            .orElseThrow(() -> new NHResidentException("입소자 리스트 찾기 실패"));

    List<ResponseResident> list = new ArrayList<>();

    for (NHResident r: residents) {
      list.add(ResponseResident
              .builder()
              .id(r.getId())
              .user_id(r.getUser().getUserId())
              .name(r.getName())
              .user_role(r.getUserRole())
              .build());
    }

    return list;
  }

  public List<ResponseResident> findAllByFacility(Long facilityId) throws Exception {
    List<NHResident> residents = nhResidentRepository.findByFacilityId(facilityId)
            .orElseThrow(() -> new NHResidentException("입소자 리스트 찾기 실패"));

    List<ResponseResident> list = new ArrayList<>();

    for (NHResident r: residents) {
      list.add(ResponseResident
              .builder()
              .id(r.getId())
              .user_id(r.getUser().getUserId())
              .name(r.getName())
              .user_role(r.getUserRole())
              .build());
    }

    return list;
  }



//    @Transactional(readOnly = true)
//    public List<NHResidentResponse> nhResidentList(Long facilityId) throws Exception {
//        List<NHResident> list = nhResidentRepository.findByFacilityId(facilityId)
//                .orElseGet(() -> new ArrayList<>());
//
//        List<NHResidentResponse> responseList = new ArrayList<>();
//
//        for (NHResident nhResident : list) {
//            NHResidentResponse response = NHResidentResponse.builder()
//                    .resident_id(nhResident.getId())
//                    .resident_name(nhResident.getName())
//                    .user_role(nhResident.getUserRole())
//                    .is_approved(nhResident.isApproved())
//                    .build();
//            responseList.add(response);
//        }
//        return responseList;
//    }

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
