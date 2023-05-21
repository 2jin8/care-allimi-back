package kr.ac.kumoh.allimi.service;

import kr.ac.kumoh.allimi.domain.*;
import kr.ac.kumoh.allimi.domain.func.Letter;
import kr.ac.kumoh.allimi.domain.func.Notice;
import kr.ac.kumoh.allimi.dto.letter.LetterEditDto;
import kr.ac.kumoh.allimi.dto.letter.LetterListDTO;
import kr.ac.kumoh.allimi.dto.letter.LetterWriteDto;
import kr.ac.kumoh.allimi.exception.*;
import kr.ac.kumoh.allimi.exception.user.UserAuthException;
import kr.ac.kumoh.allimi.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class LetterService {
  private final LetterRepository letterRepository;
  private final NHResidentRepository nhResidentRepository;

  public Long write(LetterWriteDto dto) throws Exception {  // nhresident_id, contents
    NHResident writer = nhResidentRepository.findById(dto.getNhresident_id())
            .orElseThrow(() -> new NoSuchElementException("글쓰는 사람(nhResident) not found"));

    //권한체크
    UserRole userRole = writer.getUserRole();
    if (userRole != UserRole.PROTECTOR)
      throw new UserAuthException("권한이 없는 사용자입니다.");

    //한마디 작성
    Letter letter = Letter.newLetter(writer, dto.getContents());
    Letter savedLetter = letterRepository.save(letter);

    if (savedLetter == null)
      throw new LetterException("한마디 저장 실패");

    return savedLetter.getLetterId();
  }

  public void edit(LetterEditDto dto) throws Exception {                // letter_id, writer_id, contents
    Letter letter = letterRepository.findByLetterId(dto.getLetter_id())
            .orElseThrow(() -> new NoSuchElementException("letter를 찾을 수 없음"));

    NHResident letterWriter = nhResidentRepository.findById(dto.getWriter_id())
      .orElseThrow(() -> new NoSuchElementException("해당하는 글쓴이를 찾을 수 없음"));

    if (!letter.getProtector().equals(letterWriter))
      throw new UserAuthException("권한이 없는 사용자입니다.");

    letter.edit(dto.getContents());
  }

  //한마디 목록보기
  @Transactional(readOnly = true)
  public List<LetterListDTO> letterList(Long residentId) throws Exception {
    
    NHResident writer = nhResidentRepository.findById(residentId)
      .orElseThrow(() -> new NoSuchElementException("해당하는 입소자가 없습니다"));
    UserRole userRole = writer.getUserRole();

    List<Letter> letters = new ArrayList<>();

    if (userRole == UserRole.MANAGER || userRole == UserRole.DEVELOPER) {
      letters = managerLetterList(writer);
    } else if (userRole == UserRole.WORKER) {
      letters = workerLetterList(writer);
    } else if (userRole == UserRole.PROTECTOR) {
      letters = userLetterList(writer);
    } else {
      throw new NHResidentException("user의 역할이 잘못됨");
    }

    // Response
    List<LetterListDTO> letterList = new ArrayList<>(); //letter_id, nhreaident_id, user_id, nhrname, username, contents, is_read, createDate

    for (Letter letter : letters) {
      NHResident findNHResident = letter.getProtector();
      User user = findNHResident.getUser();

      LetterListDTO dto = LetterListDTO.builder()
              .letter_id(letter.getLetterId())
              .nhresident_id(findNHResident.getId())
              .nhr_name(findNHResident.getName())
              .user_name(user.getName())
              .content(letter.getContents())
              .is_read(letter.isRead())
              .created_date(letter.getCreatedDate())
              .build();

      letterList.add(dto);
    }

    return letterList;
  }

  public List<Letter> managerLetterList(NHResident writer) { // 직원, 시설장인 경우: 시설 알림장 모두 확인 가능
    Facility facility = writer.getFacility();
    
    // 시설 전체 한마디 목록
    List<Letter> letters = letterRepository.findAllByFacility(facility).orElse(new ArrayList<Letter>());

    return letters;
  }

  public List<Letter> workerLetterList(NHResident writer) { // 직원인 경우: worker_id가 본인인 알림장 모두 확인 가능
    List<NHResident> nhResidents = nhResidentRepository.findByWorkerId(writer.getId())
            .orElse(new ArrayList<>());

    List<Letter> letters = new ArrayList<>();
    for (NHResident nhResident : nhResidents) {
      letters.addAll(letterRepository.findAllByNhResident(nhResident).orElse(new ArrayList()));
    }
    letters = letters.stream().sorted(Comparator.comparing(Letter::getCreatedDate).reversed()).collect(Collectors.toList());

    return letters;
  }

  public List<Letter> userLetterList(NHResident writer) {
    // 보호자인 경우: 자신이 쓴 한마디만 확인 가능
    // 개별 한마디 목록
    List<Letter> letters = letterRepository.findAllByNhResident(writer).orElse(new ArrayList<Notice>());

    return letters;
  }

  public Long delete(Long letter_id) {
    Long deleted = letterRepository.deleteLetterByLetterId(letter_id);
    return deleted;
  }

  public void readCheck(Long residentId, Long letterId) throws Exception {
    Letter letter = letterRepository.findById(letterId)
            .orElseThrow(() -> new NoSuchElementException("해당하는 한마디가 없음"));

    NHResident reader = nhResidentRepository.findById(residentId)
            .orElseThrow(() -> new NoSuchElementException("한마디를 읽을 nhresident가 없습니다"));

    UserRole userRole = reader.getUserRole();

    if (userRole != UserRole.MANAGER && userRole != UserRole.WORKER && userRole != UserRole.DEVELOPER) {
      throw new UserAuthException("권한이 없는 사용자입니다.");
    }

    letter.readCheck();
  }

//  public void edit(NoticeEditDto editDto) throws Exception {
//    Notice notice = letterRepository.findById(editDto.getNotice_id())
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

