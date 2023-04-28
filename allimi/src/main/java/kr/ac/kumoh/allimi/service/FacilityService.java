//package kr.ac.kumoh.allimi.service;
//
//import kr.ac.kumoh.allimi.domain.Facility;
//import kr.ac.kumoh.allimi.dto.facility.AddFacilityDTO;
//import kr.ac.kumoh.allimi.repository.FacilityRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//@Service
//@RequiredArgsConstructor
//@Transactional
//public class FacilityService {
//
//    private final FacilityRepository facilityRepository;
//
//    public Long addFacility(AddFacilityDTO dto){
//        Facility facility = Facility.makeFacility(dto.getName(), dto.getAddress(), dto.getTel(), dto.getFm_name());
//        facilityRepository.save(facility);
//
//        return facility.getId();
//    }
//}
