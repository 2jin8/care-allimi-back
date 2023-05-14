package kr.ac.kumoh.allimi.service;

import kr.ac.kumoh.allimi.controller.response.NHResidentDetailResponse;
import kr.ac.kumoh.allimi.controller.response.ResponseResident;
import kr.ac.kumoh.allimi.domain.Facility;
import kr.ac.kumoh.allimi.domain.NHResident;
import kr.ac.kumoh.allimi.domain.User;
import kr.ac.kumoh.allimi.domain.UserRole;
import kr.ac.kumoh.allimi.dto.nhresident.NHResidentUFDTO;
import kr.ac.kumoh.allimi.dto.nhresident.NHResidentDTO;
import kr.ac.kumoh.allimi.dto.nhresident.NHResidentEditDTO;
import kr.ac.kumoh.allimi.dto.nhresident.NHResidentResponse;
import kr.ac.kumoh.allimi.exception.FacilityException;
import kr.ac.kumoh.allimi.exception.NHResidentException;
import kr.ac.kumoh.allimi.exception.user.UserException;
import kr.ac.kumoh.allimi.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class NHResidentService {
    private final NHResidentRepository nhResidentRepository;
    private final UserRepository userRepository;
    private final FacilityRepository facilityRepository;

  @Transactional(readOnly = true)
  public NHResidentDetailResponse getNHResidentInfo(Long nhrId) throws Exception {
    NHResident nhResident = nhResidentRepository.findById(nhrId)
            .orElseThrow(() -> new NHResidentException("해당하는 입소자가 없습니다"));

    User user = nhResident.getUser();

    Facility facility = nhResident.getFacility();

    String name = "";
    if (nhResident.getUserRole() == UserRole.WORKER || nhResident.getUserRole() == UserRole.MANAGER) {
      name = facility.getFmName();
    } else {
      name = nhResident.getName();
    }

    //    String resident_name;
    //    String birth;
    //    String protector_name;
    //    String protector_phone_num;

    return NHResidentDetailResponse.builder()
            .resident_name(nhResident.getName())
            .birth(nhResident.getBirth())
            .protector_name(user.getName())
            .protector_phone_num(user.getPhoneNum())
            .build();
  }

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

    public void deleteResident(Long residentId) throws Exception { // 입소자 삭제
//      NHResident resident = nhResidentRepository.findById(residentId)
//              .orElseThrow(() -> new NHResidentException("해당하는 입소자가 없음"));
//      User user = resident.getUser();
      //      List<NHResidentResponse> nhResidentResponses;
//      userRepository.

      //user삭제하면 resident는 null로 설정된다!
      nhResidentRepository.deleteById(residentId);
    }

  public void editNHResident(NHResidentEditDTO dto) throws Exception { //resident_id, resident_name, birth, health_info
    NHResident nhResident = nhResidentRepository.findById(dto.getResident_id())
                    .orElseThrow(() -> new NHResidentException("해당하는 resident가 없음"));

    nhResident.edit(dto.getResident_name(), dto.getBirth(), dto.getHealth_info());
  }

  @Transactional(readOnly = true)
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
                .worker_id(r.getWorkers())
                .build());
    }

    return list;
  }

  @Transactional(readOnly = true)
  public List<ResponseResident> findAllByFacility(Long facilityId) throws Exception {
    List<NHResident> residents = nhResidentRepository.findByFacilityId(facilityId)
            .orElseThrow(() -> new NHResidentException("입소자 리스트 찾기 실패"));

    List<ResponseResident> list = new ArrayList<>();

      for (NHResident r : residents) {

          list.add(ResponseResident
                  .builder()
                  .id(r.getId())
                  .user_id(r.getUser().getUserId())
                  .name(r.getUser().getName())
                  .user_role(r.getUserRole())
                  .worker_id(r.getWorkers())
                  .build());
      }

    return list;
  }


  public NHResidentResponse changeCurrentResident(NHResidentUFDTO changeDTO) {

        User user = userRepository.findById(changeDTO.getUser_id())
                .orElseThrow(() -> new UserException("사용자 찾기 실패"));
        Facility facility = facilityRepository.findById(changeDTO.getFacility_id())
                .orElseThrow(() -> new FacilityException("시설 찾기 실패"));
        NHResident nhResident = nhResidentRepository.findById(changeDTO.getNhresident_id())
                .orElseThrow(() -> new NHResidentException("입소자 찾기 실패"));

        UserRole userRole = userRepository.getUserRole(user.getCurrentNHResident(), user.getUserId())
                .orElseThrow(() -> new UserException("역할 찾기 실패"));

        user.changeCurrNHResident(nhResident.getId());
        return NHResidentResponse.builder()
                .resident_id(nhResident.getId())
                .resident_name(nhResident.getName())
                .facility_id(facility.getId())
                .facility_name(facility.getName())
                .user_role(userRole)
                .build();
    }

    public void setWorker(NHResidentUFDTO setDTO) throws Exception {

        NHResident nhResident = nhResidentRepository.findById(setDTO.getNhresident_id())
                .orElseThrow(() -> new NHResidentException("입소자 찾기 실패"));

        NHResident userNHResident = nhResidentRepository.findNHResidentByUserIdAAndFacilityId(setDTO.getUser_id(), setDTO.getFacility_id())
                .orElseThrow(() -> new NHResidentException("userId로 입소자 찾기 실패"));

        if (nhResident.getUserRole() != UserRole.PROTECTOR || userNHResident.getUserRole() == UserRole.PROTECTOR)
            throw new UserException("역할이 올바르지 않음");

        nhResident.setWorker(userNHResident.getId());
    }

    public List<NHResidentResponse> workerList(Long userId, Long facilityId) throws Exception {
        NHResident nhResident = nhResidentRepository.findNHResidentByUserIdAAndFacilityId(userId, facilityId)
                .orElseThrow(() -> new NHResidentException("userId로 입소자 찾기 실패"));

        Long workerId = nhResident.getId();

        List<NHResident> nhResidents = nhResidentRepository.findByWorkerId(workerId)
                .orElseThrow(() -> new NHResidentException("workerId로 입소자 찾기 실패"));

        List<NHResidentResponse> responseList = new ArrayList<>();
        for (NHResident resident : nhResidents) {
            responseList.add(NHResidentResponse.builder()
                    .facility_id(resident.getFacility().getId())
                    .facility_name(resident.getFacility().getName())
                    .resident_id(resident.getId())
                    .resident_name(resident.getName())
                    .user_role(resident.getUserRole()).build());
        }
        return responseList;
    }

}
