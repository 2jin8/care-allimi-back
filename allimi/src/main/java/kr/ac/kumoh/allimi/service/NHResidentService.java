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
import kr.ac.kumoh.allimi.exception.user.UserAuthException;
import kr.ac.kumoh.allimi.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Transactional
@Slf4j
@RequiredArgsConstructor
@Service
public class NHResidentService {
  private final NHResidentRepository nhResidentRepository;
  private final UserRepository userRepository;
  private final FacilityRepository facilityRepository;

  @Transactional(readOnly = true)
  public NHResidentDetailResponse getNHResidentInfo(Long nhrId) throws Exception {
    NHResident nhResident = nhResidentRepository.findById(nhrId)
            .orElseThrow(() -> new NoSuchElementException("해당하는 입소자가 없습니다"));

    User user = nhResident.getUser();

    return NHResidentDetailResponse.builder()
            .resident_name(nhResident.getName())
            .birth(nhResident.getBirth())
            .protector_name(user.getName())
            .protector_phone_num(user.getPhoneNum())
            .build();
  }

  @Transactional
  public Long addNHResident(NHResidentDTO dto) { // user_id, facility_id, resident_name, birth, user_role, health_info;
    User user = userRepository.findUserByUserId(dto.getUser_id())
            .orElseThrow(() -> new NoSuchElementException("해당 user가 없습니다"));

    Facility facility = facilityRepository.findById(dto.getFacility_id())
            .orElseThrow(() -> new NoSuchElementException("시설을 찾을 수 없습니다"));

    //새로 만들어지면서 현재 nhResident가 얘로 바뀜
    NHResident nhResident = NHResident.newNHResident(user, facility, dto.getResident_name(), dto.getUser_role(), dto.getBirth(), dto.getHealth_info());
    nhResidentRepository.save(nhResident);

    return nhResident.getId();
  }

  public boolean findByFacilityAndUserAndUserRoleExists(Long facilityId, Long userId, UserRole userRole) {
    List<NHResident> residents = nhResidentRepository.findByFacilityAndUserAndUserRole(facilityId, userId, userRole.toString())
            .orElseGet(() -> new ArrayList<>());

    if (residents.size() == 0)
      return false;

    return true;
  }

  public void deleteResident(Long residentId) throws Exception { // 입소자 삭제
    NHResident resident = nhResidentRepository.findById(residentId)
            .orElseThrow(() -> new NoSuchElementException("해당하는 입소자가 없음"));
    User user = resident.getUser();

    //만약 user의 현재 입소자가 residentId라면 -> 삭제할거니까 오류가 발생할 수 있음
    if (user.getCurrentNhresident() == resident) {
      user.removeResident(resident);

      //user삭제하면 resident는 null로 설정된다!
      if (user.getNhResident().size() == 0) {
        //현재 입소자가 마지막 입소자라면
        user.setResidentNull();
      } else {
        user.changeCurrNHResident(user.getNhResident().get(0));
      }
    }

    nhResidentRepository.deleteById(residentId);
  }

  public void editNHResident(NHResidentEditDTO dto) throws Exception { //resident_id, resident_name, birth, health_info
    NHResident nhResident = nhResidentRepository.findById(dto.getResident_id())
            .orElseThrow(() -> new NoSuchElementException("해당하는 resident가 없음"));

    nhResident.edit(dto.getResident_name(), dto.getBirth(), dto.getHealth_info());
  }

  @Transactional(readOnly = true)
  public List<ResponseResident> findProtectorByFacility(Long facilityId) throws Exception {
    Facility facility = facilityRepository.findById(facilityId)
            .orElseThrow(() -> new NoSuchElementException("시설 찾기 실패"));

    List<NHResident> residents = nhResidentRepository.findProtectorByFacilityId(facility.getId())
            .orElseGet(() -> new ArrayList<>());

    List<ResponseResident> list = new ArrayList<>();

    for (NHResident r : residents) {
      NHResident worker = r.getWorkers();
      Long workerId = null;
      if (worker != null) {
        workerId = worker.getId();
      }

      list.add(ResponseResident
              .builder()
              .id(r.getId())
              .user_id(r.getUser().getUserId())
              .name(r.getName())
              .user_role(r.getUserRole())
              .worker_id(workerId)
              .build());
    }

    return list;
  }

  @Transactional(readOnly = true)
  public List<ResponseResident> findWorkerByFacility(Long facilityId) throws Exception {
    Facility facility = facilityRepository.findById(facilityId)
      .orElseThrow(() -> new NoSuchElementException("시설 찾기 실패"));

    List<NHResident> residents = nhResidentRepository.findWorkerByFacilityId(facility.getId())
      .orElseGet(() -> new ArrayList<>());

    List<ResponseResident> list = new ArrayList<>();


    for (NHResident r : residents) {
      User user = r.getUser();

      list.add(ResponseResident
        .builder()
        .id(r.getId())
        .user_id(r.getUser().getUserId())
        .name(user.getName())
        .user_role(r.getUserRole())
        .worker_id(null)
        .build());
    }

    return list;
  }

  @Transactional(readOnly = true)
  public List<ResponseResident> findAllByFacility(Long facilityId) throws Exception {
    Facility facility = facilityRepository.findById(facilityId)
            .orElseThrow(() -> new NoSuchElementException("시설 찾기 실패"));

    List<NHResident> residents = nhResidentRepository.findByFacilityId(facility.getId())
            .orElseGet(() -> new ArrayList<>());

    List<ResponseResident> list = new ArrayList<>();

    for (NHResident r : residents) {
      list.add(ResponseResident
              .builder()
              .id(r.getId())
              .user_id(r.getUser().getUserId())
              .name(r.getUser().getName())
              .user_role(r.getUserRole())
              .worker_id(r.getWorkers().getId())
              .build());
    }

    return list;
  }

  public void setWorker(NHResidentUFDTO setDTO) throws Exception {  // resdient_id, worker_id, facility_id;
    NHResident nhResident = nhResidentRepository.findById(setDTO.getResdient_id())
            .orElseThrow(() -> new NoSuchElementException("입소자 찾기 실패"));

    NHResident worker =  nhResidentRepository.findById(setDTO.getWorker_id())
            .orElseThrow(() -> new NoSuchElementException("직원을 찾을 수 없음"));

    UserRole userRole = worker.getUserRole();

    //user가 요양보호사가 아니거나 resident가 보호자가 아니라면
    if (nhResident.getUserRole() != UserRole.PROTECTOR || userRole == UserRole.PROTECTOR) {
      log.info("" + nhResident.getUserRole() + "/ " + userRole);
      throw new UserAuthException("역할이 올바르지 않음");
    }

    nhResident.setWorker(worker);
  }

  public List<NHResidentResponse> workerList(Long workerId) throws Exception {
    List<NHResident> nhResidents = nhResidentRepository.findByWorkerId(workerId)
            .orElseGet(() -> new ArrayList<>());

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


//
//  public NHResidentResponse changeCurrentResident(NHResidentUFDTO changeDTO) throws Exception {
//
//    User user = userRepository.findById(changeDTO.getUser_id())
//            .orElseThrow(() -> new UserException("사용자 찾기 실패"));
//    Facility facility = facilityRepository.findById(changeDTO.getFacility_id())
//            .orElseThrow(() -> new FacilityException("시설 찾기 실패"));
//    NHResident nhResident = nhResidentRepository.findById(changeDTO.getNhresident_id())
//            .orElseThrow(() -> new NHResidentException("입소자 찾기 실패"));
//
//    UserRole userRole = userRepository.getUserRole(user.getCurrentNHResident(), user.getUserId())
//            .orElseThrow(() -> new UserException("역할 찾기 실패"));
//
//    user.changeCurrNHResident(nhResident.getId());
//    return NHResidentResponse.builder()
//            .resident_id(nhResident.getId())
//            .resident_name(nhResident.getName())
//            .facility_id(facility.getId())
//            .facility_name(facility.getName())
//            .user_role(userRole)
//            .build();
//  }


}
