package kr.ac.kumoh.allimi.controller;

import kr.ac.kumoh.allimi.dto.facility.AddFacilityDTO;
import kr.ac.kumoh.allimi.service.FacilityService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class FacilityController {
    private final FacilityService facilityService;

    //시설 추가
    @PostMapping("/v2/facility")
    public ResponseEntity addFacility(@RequestBody AddFacilityDTO dto) { // name, address, tel, fm_name
        Long facilityId;

        try {
            facilityId = facilityService.addFacility(dto);
        } catch(Exception exception) { //그냥 에러
          log.info("FacilityController 시설추가: 에러남");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

      Map<String, Long> map = new HashMap<>();
      map.put("facility_id", facilityId);

        return ResponseEntity.status(HttpStatus.OK).body(map);
    }
}
