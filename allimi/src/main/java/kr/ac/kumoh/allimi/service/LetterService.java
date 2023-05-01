package kr.ac.kumoh.allimi.service;

import kr.ac.kumoh.allimi.domain.*;
import kr.ac.kumoh.allimi.domain.func.Letter;
import kr.ac.kumoh.allimi.domain.func.Notice;
import kr.ac.kumoh.allimi.dto.letter.LetterEditDto;
import kr.ac.kumoh.allimi.dto.letter.LetterListDTO;
import kr.ac.kumoh.allimi.dto.letter.LetterWriteDto;
import kr.ac.kumoh.allimi.dto.notice.NoticeListDTO;
import kr.ac.kumoh.allimi.exception.FacilityException;
import kr.ac.kumoh.allimi.exception.LetterException;
import kr.ac.kumoh.allimi.exception.NHResidentException;
import kr.ac.kumoh.allimi.exception.user.UserAuthException;
import kr.ac.kumoh.allimi.exception.user.UserException;
import kr.ac.kumoh.allimi.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class LetterService {
  private final LetterRepository letterRepository;
  private final UserRepository userRepository;
  private final FacilityRepository facilityRepository;
  private final NHResidentRepository nhResidentRepository;

  public Long write(LetterWriteDto dto) throws Exception {  // user_id, nhresident_id, facility_id, contents
    User user = userRepository.findUserByUserId(dto.getUser_id())
            .orElseThrow(() -> new UserException("user not found"));

    NHResident targetResident = nhResidentRepository.findById(dto.getNhresident_id())
            .orElseThrow(() -> new NHResidentException("target resident not found"));

    Facility facility = facilityRepository.findById(dto.getFacility_id())
            .orElseThrow(() -> new FacilityException("facility not found"));

    Letter letter = Letter.newLetter(user, targetResident, facility, dto.getContents());
    Letter savedLetter = letterRepository.save(letter);

    if (savedLetter == null)
      throw new LetterException("한마디 저장 실패");

    return savedLetter.getLetterId();
  }

  public void edit(LetterEditDto dto) throws Exception {  // // letter_id, user_id, nhresident_id, contents
    Letter letter = letterRepository.findByLetterId(dto.getLetter_id())
            .orElseThrow(() -> new LetterException("letter를 찾을 수 없음"));

    User user = letter.getUser();

    if (!letter.getUser().equals(user))
      new UserAuthException("권한이 없는 사용자입니다.");

    NHResident resident = nhResidentRepository.findById(dto.getNhresident_id())
            .orElseThrow(() -> new NHResidentException("resident를 찾을 수 없음"));

    letter.edit(resident, dto.getContents());
  }


  //한마디 목록보기
  @Transactional(readOnly = true)
  public List<LetterListDTO> letterList(Long residentId) throws Exception {
    NHResident nhResident = nhResidentRepository.findById(residentId)
            .orElseThrow(() -> new NHResidentException("nhResident를 찾을 수 없음"));

    List<Letter> letters = null;
    UserRole userRole = nhResident.getUserRole();

    if (userRole == UserRole.MANAGER || userRole == UserRole.WORKER) {
      letters = managerLetterList(nhResident);
    } else if (userRole == UserRole.PROTECTOR) {
      letters = userLetterList(nhResident);
    } else {
      throw new NHResidentException("user의 역할이 잘못됨");
    }

    // Response
    List<LetterListDTO> letterList = new ArrayList<>(); //letter_id, nhreaident_id, user_id, nhrname, username, contents, is_read, createDate

    for (Letter letter : letters) {
      User user = letter.getUser();
      NHResident findNHResident = letter.getNhResident();

      LetterListDTO dto = LetterListDTO.builder()
              .letter_id(letter.getLetterId())
              .nhreaident_id(findNHResident.getId())
              .user_id(user.getUserId())
              .nhr_name(findNHResident.getName())
              .user_name(user.getName())
              .content(letter.getContents())
              .is_read(letter.isRead())
              .create_date(letter.getCreateDate())
              .build();

      letterList.add(dto);
    }

    return letterList;
  }

  public List<Letter> managerLetterList(NHResident nhResident) { // 직원, 시설장인 경우: 시설 알림장 모두 확인 가능
    Facility facility = nhResident.getFacility();
    // 시설 전체 한마디 목록
    List<Letter> letters = letterRepository.findAllByFacility(facility).orElse(new ArrayList<Notice>());

    return letters;
  }

  public List<Letter> userLetterList(NHResident nhResident) {
    // 보호자인 경우: 개별 알림장만 확인 가능
    // 개별 알림장 목록
    List<Letter> letters = letterRepository.findAllByNhResident(nhResident).orElse(new ArrayList<Notice>());

    return letters;
  }

  public void readCheck(Long userId, Long letterId) throws Exception {
    User user = userRepository.findUserByUserId(userId)
            .orElseThrow(() -> new UserException("해당하는 user가 없습니다"));

    List<UserRole> userRoleList = userRepository.getUserRole(userId)
            .orElseThrow(() -> new UserException("userRole이 잘못됨"));

    UserRole userRole = userRoleList.get(0);

    if (userRole != UserRole.MANAGER || userRole != UserRole.WORKER)
      new UserAuthException("권한이 없는 사용자");

    Letter letter = letterRepository.findById(letterId)
            .orElseThrow(() -> new LetterException("해당하는 한마디가 없음"));

    letter.readCheck();
  }

//  public void edit(NoticeEditDto editDto) throws Exception {
//    Notice notice = noticeRepository.findById(editDto.getNotice_id())
//            .orElseThrow(() -> new NoticeException("해당 notice가 없습니다"));
//    User writer = notice.getUser();
//
//    User user = userRepository.findUserByUserId(editDto.getUser_id())
//            .orElseThrow(()-> new UserException("없는 사용자입니다"));
//
//    if (writer.getUserId() != editDto.getUser_id() && user.getUserRole() != UserRole.MANAGER)
//      throw new UserException("권한이 없는 사용자 입니다");
//
//
//    NHResident targetResident = nhResidentRepository.findById(editDto.getResident_id())
//            .orElseThrow(() -> new NHResidentException("입소자를 찾을 수 없습니다"));
//
//    notice.editNotice(targetResident, editDto.getContent(), editDto.getSub_content(), editDto.getImage_url());
//  }
//
//  public Long delete(Long notice_id) {
//    Notice notice = noticeRepository.findById(notice_id).
//            orElseThrow(() -> new NoticeException("해당 Notice가 없습니다"));
//    String imageUrl = notice.getImageUrl();
//    if (imageUrl != null) {
//      s3Service.delete(imageUrl.substring(59));
//    }
//    Long deleted = noticeRepository.deleteNoticeById(notice_id);
//    return deleted;
//  }
}

