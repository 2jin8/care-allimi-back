package kr.ac.kumoh.allimi.service;

import kr.ac.kumoh.allimi.domain.Facility;
import kr.ac.kumoh.allimi.dto.facility.AddFacilityDTO;
import kr.ac.kumoh.allimi.dto.facility.EditFacilityDTO;
import kr.ac.kumoh.allimi.exception.FacilityException;
import kr.ac.kumoh.allimi.repository.FacilityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FacilityService {
  private final FacilityRepository facilityRepository;

  public Long addFacility(AddFacilityDTO dto){ // name, address, tel, fm_name
      Facility facility = Facility.makeFacility(dto.getName(), dto.getAddress(), dto.getTel(), dto.getFm_name());
      facilityRepository.save(facility);

      return facility.getId();
  }

  public Long editFacility(EditFacilityDTO dto){ // facility_id, name, address, tel, fm_name
    Facility facility = facilityRepository.findById(dto.getFacility_id())
                    .orElseThrow(() -> new FacilityException("시설을 찾을 수 없음"));

    facility.edit(dto.getName(), dto.getAddress(), dto.getTel(), dto.getFm_name());

    return facility.getId();
  }

  @Transactional(readOnly = true)
  public Page<Facility> findAll(Pageable pageable) throws Exception {
    Page<Facility> facilities = facilityRepository.findAll(pageable);

    return facilities;
  }

  @Transactional
  public void deleteFacility(Long facility_id) throws Exception { // 회원탈퇴
    facilityRepository.deleteById(facility_id);
  }
}
