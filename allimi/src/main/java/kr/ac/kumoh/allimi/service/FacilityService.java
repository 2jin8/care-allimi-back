package kr.ac.kumoh.allimi.service;

import kr.ac.kumoh.allimi.domain.Facility;
import kr.ac.kumoh.allimi.domain.User;
import kr.ac.kumoh.allimi.dto.facility.AddFacilityDTO;
import kr.ac.kumoh.allimi.dto.facility.EditFacilityDTO;
import kr.ac.kumoh.allimi.dto.facility.FacilityInfoDto;
import kr.ac.kumoh.allimi.exception.FacilityException;
import kr.ac.kumoh.allimi.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional
public class FacilityService {
  private final FacilityRepository facilityRepository;
//  private final NHResidentRepository nhResidentRepository;
//  private final InvitationRepository invitationRepository;
//  private final LetterRepository letterRepository;
//  private final NoticeRepository noticeRepository;
//  private final VisitRepository visitRepository;
//  private final AllNoticeRepository allNoticeRepository;

  public Long addFacility(AddFacilityDTO dto){ // name, address, tel, fm_name
    Facility facility = Facility.makeFacility(dto.getName(), dto.getAddress(), dto.getTel(), dto.getFm_name());
    facilityRepository.save(facility);

    return facility.getId();
  }

  public FacilityInfoDto getInfo(Long facilityId) throws Exception {
    Facility facility = facilityRepository.findById(facilityId)
            .orElseThrow(() -> new NoSuchElementException("해당하는 시설을 찾을 수 없음"));

    FacilityInfoDto dto = FacilityInfoDto.builder()
            .name(facility.getName())
            .tel(facility.getTel())
            .address(facility.getAddress())
            .fm_name(facility.getFmName())
            .build();

    return dto;
  }

  public Long editFacility(EditFacilityDTO dto) throws Exception { // facility_id, name, address, tel, fm_name
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

  @Transactional(readOnly = true)
  public Page<Facility> findAllAdmin(Pageable pageable) throws Exception {
    int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
    pageable = PageRequest.of(page, 10);
    Page<Facility> facilities = facilityRepository.findAll(pageable);

    return facilities;
  }

  @Transactional(readOnly = true)
  public Page<Facility> getSearchFacility(String searchKeyword, Pageable pageable) {
    int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
    pageable = PageRequest.of(page, 10);

    Page<Facility> facilities = facilityRepository.findByNameContaining(searchKeyword, pageable).orElse(null);

    return facilities;
  }

  @Transactional
  public void deleteFacility(Long facility_id) throws Exception { // 회원탈퇴
//    Facility facility = facilityRepository.findById(facility_id)
//            .orElseThrow(() -> new FacilityException("시설을 찾을 수 없음"));
//    //딸린 한마디 삭제
//    List<Letter> letters = letterRepository.findAllByFacility(facility)
//            .orElseGet(() -> new ArrayList<>());
//
//    for (Letter letter : letters) {
//      letterRepository.deleteById(letter.getLetterId());
//    }
//
//    //딸린 면회 삭제
//    List<Visit> visits = visitRepository.findAllByFacility(facility)
//            .orElseGet(() -> new ArrayList<>());
//
//    for (Visit visit : visits) {
//      visitRepository.deleteById(visit.getId());
//    }
//
//    //딸린 알림장 삭제
//    List<Notice> notices = noticeRepository.findAllByFacility(facility)
//            .orElseGet(() -> new ArrayList<>());
//
//    for (Notice not : notices) {
//      noticeRepository.deleteById(not.getNoticeId());
//    }
//
//    //딸린 전체공지 삭제
//    List<AllNotice> allNotices = allNoticeRepository.findByFacility(facility)
//            .orElseGet(() -> new ArrayList<>());
//
//    for (AllNotice allnot : allNotices) {
//      allNoticeRepository.deleteById(allnot.getAllNoticeId());
//    }
//
//    //딸린 초대 삭제
//    List<Invitation> invitations = invitationRepository.findByFacilityId(facility_id)
//            .orElseGet(() -> new ArrayList<>());
//
//    for (Invitation invit : invitations) {
//      invitationRepository.deleteById(invit.getId());
//    }
//
//    //딸린 입소자 삭제
//    List<NHResident> residents = nhResidentRepository.findProtectorByFacilityId(facility_id)
//            .orElseThrow(() -> new NHResidentException("입소자 리스트 찾기 실패"));
//
//    for (NHResident nhResident : residents) {
//      nhResidentRepository.deleteById(nhResident.getId());
//    }

    facilityRepository.deleteById(facility_id);
  }
}
