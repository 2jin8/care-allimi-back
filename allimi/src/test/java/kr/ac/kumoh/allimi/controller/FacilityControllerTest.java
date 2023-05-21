//package kr.ac.kumoh.allimi.controller;
//
//import kr.ac.kumoh.allimi.dto.facility.AddFacilityDTO;
//import kr.ac.kumoh.allimi.dto.facility.EditFacilityDTO;
//import kr.ac.kumoh.allimi.dto.facility.FacilityInfoDto;
//import kr.ac.kumoh.allimi.service.FacilityService;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.annotation.Rollback;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.NoSuchElementException;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//
//@SpringBootTest
//@Slf4j
//public class FacilityControllerTest {
//  @Autowired
//  FacilityService facilityService;
//
//  @Test
//  @BeforeEach //모든 테스트 시작 전에 테스트함
//  @Transactional
//  @DisplayName("시설 추가 테스트")
//  @Rollback(true)
//  public void addFacility() {
//    log.info("시설 추가 테스트함@@");
//    AddFacilityDTO dto = AddFacilityDTO.builder()
//            .name("금오요양원")
//            .address("구미시")
//            .tel("01000000000")
//            .fm_name("시설장명")
//            .build();
//
//    Long facilityId = facilityService.addFacility(dto);
//
//    FacilityInfoDto dto2 = facilityService.getInfo(facilityId);
//
//    assertThat("금오요양원").isEqualTo(dto2.getName());
//    assertThat("구미시").isEqualTo(dto2.getAddress());
//    assertThat("01000000000").isEqualTo(dto2.getTel());
//    assertThat("시설장명").isEqualTo(dto2.getFm_name());
//  }
//
//  //add 테스트가 잘 실행된다는 가정하에 테스트 진행
//  @Test
//  @Transactional
//  @DisplayName("시설 삭제 테스트")
//  @Rollback(true)
//  public void deleteFacility() {
//    AddFacilityDTO dto = AddFacilityDTO.builder()
//            .name("금오요양원")
//            .address("구미시")
//            .tel("01000000000")
//            .fm_name("시설장명")
//            .build();
//
//    Long facilityId = facilityService.addFacility(dto);
//
//    try {
//      facilityService.deleteFacility(facilityId);
//    } catch (Exception e){
//      assertThat("금오요양원").isEqualTo("");
//    }
//
//    try {
//      FacilityInfoDto dto3 = facilityService.getInfo(facilityId);
//      assertThat("금오요양원").isEqualTo("");
//    } catch( NoSuchElementException e) {
//      assertThat("").isEqualTo("");
//    } catch (Exception e) {
//      assertThat("금오요양원").isEqualTo("");
//    }
//  }
//
//  @Test
//  @Transactional
//  @DisplayName("시설 수정 테스트")
//  @Rollback(true)
//  public void editFacility() {
//    log.info("시설 수정 테스트함@@");
//    AddFacilityDTO dto = AddFacilityDTO.builder()
//            .name("금오요양원")
//            .address("구미시")
//            .tel("01000000000")
//            .fm_name("시설장명")
//            .build();
//
//    Long facilityId = facilityService.addFacility(dto);
//
//
//    //전체변경 시나리오
//    EditFacilityDTO editDto = EditFacilityDTO.builder()
//            .facility_id(facilityId)
//            .name("쿠로미 요양원")
//            .address("안녕")
//            .tel("01011")
//            .fm_name("바뀐 시설장")
//            .build();
//
//    facilityService.editFacility(editDto);
//
//    FacilityInfoDto dto2 = facilityService.getInfo(facilityId);
//
//    assertThat("쿠로미 요양원").isEqualTo(dto2.getName());
//    assertThat("안녕").isEqualTo(dto2.getAddress());
//    assertThat("01011").isEqualTo(dto2.getTel());
//    assertThat("바뀐 시설장").isEqualTo(dto2.getFm_name());
//
//    //부분변경 시나리오
//    editDto = EditFacilityDTO.builder()
//            .facility_id(facilityId)
//            .name("마멜 요양원")
//            .build();
//
//    facilityService.editFacility(editDto);
//
//    dto2 = facilityService.getInfo(facilityId);
//
//    assertThat("마멜 요양원").isEqualTo(dto2.getName());
//    assertThat("안녕").isEqualTo(dto2.getAddress());
//    assertThat("01011").isEqualTo(dto2.getTel());
//    assertThat("바뀐 시설장").isEqualTo(dto2.getFm_name());
//  }
//}
