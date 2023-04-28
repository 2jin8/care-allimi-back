//package kr.ac.kumoh.allimi.controller;
//
//import kr.ac.kumoh.allimi.dto.facility.AddFacilityDTO;
//import kr.ac.kumoh.allimi.service.FacilityService;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequiredArgsConstructor
//@CrossOrigin(origins = "*", allowedHeaders = "*")
//public class FacilityController {
//    private final FacilityService facilityService;
//
//    //시설 추가
//    @PostMapping("/v2/facility")
//    public ResponseEntity addFacility(@RequestBody AddFacilityDTO dto) {
//        Long facilityId;
//
//        try {
//            facilityId = facilityService.addFacility(dto);
//        } catch(Exception exception) { //그냥 에러
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//
//        return ResponseEntity.status(HttpStatus.OK).body(new ResponseFacility(facilityId));
//    }
//
//    @Getter
//    @AllArgsConstructor
//    public class ResponseFacility {
//        private Long facility_id;
//    }
//
//
//}
