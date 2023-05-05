package kr.ac.kumoh.allimi.service;
import kr.ac.kumoh.allimi.domain.*;
import kr.ac.kumoh.allimi.domain.func.Notice;
import kr.ac.kumoh.allimi.dto.notice.NoticeEditDto;
import kr.ac.kumoh.allimi.dto.notice.NoticeListDTO;
import kr.ac.kumoh.allimi.controller.response.NoticeResponse;
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

  public Long write(NoticeWriteDto dto, List<MultipartFile> files) throws Exception {  // notice{user_id, nhresident_id, facility_id, contents, sub_contents}, file{}
    User user = userRepository.findUserByUserId(dto.getUser_id())
            .orElseThrow(() -> new UserException("user not found"));

    UserRole userRole = userRepository.getUserRole(user.getCurrentNHResident(), user.getUserId())
            .orElseThrow(() -> new UserException("역할을 찾을 수 없습니다."));

    if (userRole != UserRole.MANAGER && userRole != UserRole.WORKER)
      throw new UserAuthException("권한이 없는 사용자입니다.");

    NHResident targetResident = nhResidentRepository.findById(dto.getNhresident_id())
            .orElseThrow(() -> new NHResidentException("target resident not found"));

    Facility facility = facilityRepository.findById(dto.getFacility_id())
            .orElseThrow(() -> new FacilityException("facility not found"));

    Notice notice = Notice.newNotice(user, targetResident, facility, dto.getContents(), dto.getSub_contents());

    List<Image> images = new ArrayList<>();

    if (files != null) {
      for (MultipartFile file : files) {
        if (!file.isEmpty()) {
          String url = URLDecoder.decode(s3Service.upload(file), "utf-8");
          Image image = Image.newNoticeImage(notice, url);
          images.add(image);
          imageRepository.save(image);
        }
      }
    } else {
      System.out.println("@@@@@@@@@@@@@@@@@@ file null");
    }

    notice.addImages(images);
    Notice savedNotice = noticeRepository.save(notice);

    if (savedNotice == null)
      throw new NoticeException("알림장 저장 실패");

    return savedNotice.getNoticeId();
  }

  //알림장 목록보기
  public List<NoticeListDTO> noticeList(Long residentId) throws Exception {
    NHResident nhResident = nhResidentRepository.findById(residentId)
            .orElseThrow(() -> new NHResidentException("nhResident를 찾을 수 없음"));

    List<NoticeListDTO> notices = new ArrayList<>();
    UserRole userRole = nhResident.getUserRole();

    if (userRole == UserRole.MANAGER || userRole == UserRole.WORKER) { // 직원, 시설장인 경우: 시설 알림장 모두 확인 가능
      Facility facility = nhResident.getFacility();
      List<Notice> managerNoticeList = noticeRepository.findAllByFacility(facility).orElse(new ArrayList<Notice>());
      notices = noticeList(managerNoticeList);
    } else if (userRole == UserRole.PROTECTOR) { // 보호자인 경우: 개별 알림장만 확인 가능
      List<Notice> userNoticeList = noticeRepository.findAllByTarget(nhResident).orElse(new ArrayList<Notice>());
      notices = noticeList(userNoticeList);
    } else {
      throw new NHResidentException("user의 역할이 잘못됨");
    }

    return notices;
  }

  public List<NoticeListDTO> noticeList(List<Notice> notices) {
    List<NoticeListDTO> noticeList = new ArrayList<>();

    for (Notice notice : notices) {
      List<Image> images = imageRepository.findAllByNotice(notice).orElse(new ArrayList<>());
      List<String> urls = new ArrayList<>();
      for (Image image : images) {
        urls.add(image.getImageUrl());
      }

      User user = notice.getUser();

      NoticeListDTO dto = NoticeListDTO.builder()
              .noticeId(notice.getNoticeId())
              .create_date(notice.getCreateDate())
              .content(notice.getContents())
              .imageUrl(urls)
              .user_name(user.getName())
              .build();
      noticeList.add(dto);
    }

    return noticeList;
  }


  //알림장 상세보기
  public NoticeResponse getDetail(Long noticeId) throws Exception {
    Notice notice = noticeRepository.findById(noticeId).orElseThrow(() -> new NoticeException("해당 알림장을 찾을 수 없습니다"));
    User user = notice.getUser(); // 작성한 사람

    List<Image> images = imageRepository.findAllByNotice(notice).orElse(new ArrayList<>());
    List<String> images_url = new ArrayList<>();
    for (Image image : images) {
      images_url.add(image.getImageUrl());
    }

    return NoticeResponse.builder()
            .create_date(notice.getCreateDate())
            .user_id(user.getUserId())
            .notice_id(notice.getNoticeId())
            .sub_content(notice.getSubContents())
            .content(notice.getContents())
            .image_url(images_url)
            .build();
  }

  public Long edit(NoticeEditDto editDto, List<MultipartFile> files) throws Exception {
    Notice notice = noticeRepository.findNoticeByNoticeId(editDto.getNotice_id())
            .orElseThrow(() -> new NoticeException("해당 notice가 없습니다"));

    User user = userRepository.findById(editDto.getUser_id())
            .orElseThrow(() -> new UserException("사용자를 찾을 수 없습니다."));

    UserRole userRole = userRepository.getUserRole(user.getCurrentNHResident(), user.getUserId())
            .orElseThrow(() -> new UserException("역할을 찾을 수 없습니다."));

    User writer = notice.getUser();

    if (writer.getUserId() != user.getUserId() || userRole != UserRole.MANAGER && userRole != UserRole.WORKER)
      throw new UserAuthException("권한이 없는 사용자입니다.");

    NHResident targetResident = nhResidentRepository.findById(editDto.getResident_id())
            .orElseThrow(() -> new NHResidentException("입소자를 찾을 수 없습니다"));

    // 기존 DB, S3에 저장된 이미지 삭제
    List<Image> deleteList = imageRepository.findAllByNotice(notice).orElse(new ArrayList<>());
    for (Image image : deleteList) {
      String url = image.getImageUrl();
      s3Service.delete(url.substring(59)); // S3에 저장된 기존 이미지 삭제
      imageRepository.deleteImageByImageId(image.getImageId()); // Image DB에 저장된 기존 이미지 삭제
    }

    // 수정된 이미지 넣기
    List<String> images_url = new ArrayList<>();
    for (MultipartFile file : files) {
      if (!file.isEmpty()) {
        String url = URLDecoder.decode(s3Service.upload(file), "utf-8");
        Image image = Image.newNoticeImage(notice, url);
        images_url.add(url);
        imageRepository.save(image);
      }
    }

    notice.editNotice(targetResident, editDto.getContent(), editDto.getSub_content(), images_url);
    System.out.println("notice id: " + notice.getNoticeId());
    return notice.getNoticeId();
  }

  public Long delete(Long notice_id) {
    Notice notice = noticeRepository.findById(notice_id).
            orElseThrow(() -> new NoticeException("해당 Notice가 없습니다"));
    List<Image> images = imageRepository.findAllByNotice(notice).orElse(new ArrayList<>());

    for (Image image : images) {
      String image_url = image.getImageUrl();
      s3Service.delete(image_url.substring(59));
      imageRepository.deleteImageByImageId(image.getImageId());
    }

    Long deleted = noticeRepository.deleteNoticeByNoticeId(notice_id);
    return deleted;
  }
}

