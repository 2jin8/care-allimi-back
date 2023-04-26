package kr.ac.kumoh.allimi.service;
import kr.ac.kumoh.allimi.domain.*;
import kr.ac.kumoh.allimi.dto.notice.NoticeListDTO;
import kr.ac.kumoh.allimi.dto.notice.NoticeWriteDto;
import kr.ac.kumoh.allimi.exception.*;
import kr.ac.kumoh.allimi.exception.user.UserAuthException;
import kr.ac.kumoh.allimi.exception.user.UserException;
import kr.ac.kumoh.allimi.repository.*;
import kr.ac.kumoh.allimi.s3.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class NoticeService {
  private final NoticeRepository noticeRepository;
  private final UserRepository userRepository;
  private final FacilityRepository facilityRepository;
  private final NHResidentRepository nhResidentRepository;
  private final ImageRepository imageRepository;
  private final S3Service s3Service;

  public void write(NoticeWriteDto dto, List<MultipartFile> files) throws Exception {
    User user = userRepository.findUserByUserId(dto.getUser_id())
            .orElseThrow(() -> new UserException("user not found"));

    if (user.getUserRole() != UserRole.MANAGER && user.getUserRole() != UserRole.WORKER) {
      throw new UserAuthException("권한이 없는 사용자 입니다.");
    }

    NHResident targetResident = nhResidentRepository.findById(dto.getTarget_id())
            .orElseThrow(() -> new NHResidentException("target resident not found"));

    Facility facility = facilityRepository.findById(dto.getFacility_id())
            .orElseThrow(() -> new FacilityException("facility not found"));

    Notice notice = Notice.newNotice(user, targetResident, facility, dto.getContents(), dto.getSub_contents());
    List<Image> images = new ArrayList<>();
    if (files.size() != 0) {
      for (MultipartFile file : files) {
        String url = URLDecoder.decode(s3Service.upload(file), "utf-8");
        Image image = Image.newNoticeImage(notice, url);
        images.add(image);
        imageRepository.save(image);
      }
    }

    notice.addImages(images);
    Notice savedNotice = noticeRepository.save(notice);
    
    if (savedNotice == null)
      throw new NoticeException("알림장 저장 실패");
  }
  
  //알림장 목록보기
  public List<NoticeListDTO> noticeList(Long residentId) throws Exception {
    NHResident nhResident = nhResidentRepository.findById(residentId)
            .orElseThrow(() -> new NHResidentException("nhResident를 찾을 수 없음"));

    List<Notice> notices = new ArrayList<>();
    UserRole userRole = nhResident.getUserRole();

    if (userRole == UserRole.MANAGER || userRole == UserRole.WORKER) {
      notices = managerNoticeList(nhResident);
    } else if (userRole == UserRole.PROTECTOR) {
      notices = userNoticeList(nhResident);
    } else {
      throw new NHResidentException("user의 역할이 잘못됨");
    }

    // Response
    List<NoticeListDTO> noticeList = new ArrayList<>();

    for (Notice notice : notices) {

      List<Image> imgList = notice.getImages();
      List<String> imgStringList = new ArrayList<>();

      for (Image img:imgList) {
        imgStringList.add(img.getImageUrl());
      }

      NoticeListDTO dto = NoticeListDTO.builder()
              .noticeId(notice.getId())
              .create_date(notice.getCreateDate())
              .content(notice.getContents())
              .imageUrl(imgStringList)
              .build();

      noticeList.add(dto);
    }

    return noticeList;
  }

  public List<Notice> managerNoticeList(NHResident nhResident) { // 직원, 시설장인 경우: 시설 알림장 모두 확인 가능
    Facility facility = nhResident.getFacility();
    // 시설 알림장 목록
    List<Notice> notices = noticeRepository.findAllByFacility(facility).orElse(new ArrayList<Notice>());

    return notices;
  }

  public List<Notice> userNoticeList(NHResident nhResident) {
    // 보호자인 경우: 개별 알림장만 확인 가능
    // 개별 알림장 목록
    List<Notice> notices = noticeRepository.findAllByTarget(nhResident).orElse(new ArrayList<Notice>());

    return notices;
  }

//  //알림장 상세보기
//  public NoticeResponse getDetail(Long noticeId) throws Exception {
//    Notice notice = noticeRepository.findById(noticeId).orElseThrow(() -> new NoticeException("해당 알림장을 찾을 수 없습니다"));
//    User user = notice.getUser();
//
//    return NoticeResponse.builder()
//            .create_date(notice.getCreateDate())
//            .user_id(user.getUserId())
//            .notice_id(notice.getId())
//            .sub_content(notice.getSubContents())
//            .content(notice.getContents())
//            .image_url(notice.getImageUrl())
//            .build();
//  }
//
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

