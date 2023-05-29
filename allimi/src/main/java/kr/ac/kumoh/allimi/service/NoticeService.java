package kr.ac.kumoh.allimi.service;

import kr.ac.kumoh.allimi.domain.*;
import kr.ac.kumoh.allimi.domain.func.Image;
import kr.ac.kumoh.allimi.domain.func.Notice;
import kr.ac.kumoh.allimi.dto.NoticeDTO;
import kr.ac.kumoh.allimi.controller.response.NoticeResponse;
import kr.ac.kumoh.allimi.exception.*;
import kr.ac.kumoh.allimi.exception.user.UserAuthException;
import kr.ac.kumoh.allimi.repository.*;
import kr.ac.kumoh.allimi.s3.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

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

  public Long write(NoticeDTO.Write dto, List<MultipartFile> files) throws Exception {  // notice{writer_id, target_id, contents, sub_contents}, file{}
    NHResident writer = nhResidentRepository.findById(dto.getWriter_id())
            .orElseThrow(() -> new NoSuchElementException("입소자 찾기 실패 - writer_id에 해당하는 입소자 없음"));

    NHResident target = nhResidentRepository.findById(dto.getTarget_id())
            .orElseThrow(() -> new NoSuchElementException("입소자 찾기 실패 - target_id에 해당하는 입소자 없음"));

    if (writer.getUserRole() != UserRole.MANAGER && writer.getUserRole() != UserRole.WORKER)
      throw new UserAuthException("알림장 작성 실패 - 권한이 없는 사용자");

    Notice notice = Notice.newNotice(writer, target, dto.getContents(), dto.getSub_contents());

    List<Image> images = new ArrayList<>();
    if (files != null) {
      if (files.size() > 10)
        throw new FileCountExceedException("사진의 최대 업로드 수는 10장입니다.");

      try {
        for (MultipartFile file : files) {
          if (!file.isEmpty()) {
            String url = URLDecoder.decode(s3Service.upload(file), "utf-8");
            Image image = Image.newNoticeImage(notice, url);
            images.add(image);
            imageRepository.save(image);
          }
        }
      } catch(Exception e) {
        throw new Exception();
      }
    }

    notice.addImages(images);
    Notice savedNotice = noticeRepository.save(notice);

    if (savedNotice == null)
      throw new InternalException("알림장 저장 실패");

    return savedNotice.getNoticeId();
  }

  //알림장 목록보기
  public List<NoticeDTO.ListAll> noticeList(Long residentId) throws Exception {
    NHResident nhResident = nhResidentRepository.findById(residentId)
            .orElseThrow(() -> new NoSuchElementException("입소자 찾기 실패 - resident_id에 해당하는 입소자 없음"));

    List<NoticeDTO.ListAll> notices = new ArrayList<>();
    UserRole userRole = nhResident.getUserRole();

    if (userRole == UserRole.MANAGER || userRole == UserRole.WORKER) { // 직원, 시설장인 경우: 시설 알림장 모두 확인 가능
      Facility facility = nhResident.getFacility();

      List<NHResident> nhResidents = nhResidentRepository.findByFacilityId(facility.getId())
              .orElseThrow(() -> new NHResidentException("입소자 찾기 실패 - 시설에 해당하는 입소자 찾기 실패"));

      List<Notice> managerNoticeList = new ArrayList<>();
      for (NHResident resident : nhResidents) {
        managerNoticeList.addAll(noticeRepository.findAllByTarget(resident).orElse(new ArrayList<>()));
      }
      managerNoticeList = managerNoticeList.stream().sorted(Comparator.comparing(Notice::getCreatedDate).reversed()).collect(Collectors.toList());
      notices = noticeList(managerNoticeList);

    } else if (userRole == UserRole.PROTECTOR) { // 보호자인 경우: 개별 알림장만 확인 가능
      List<Notice> userNoticeList = noticeRepository.findAllByTarget(nhResident).orElse(new ArrayList<>());
      notices = noticeList(userNoticeList);
    } else {
      throw new NHResidentException("잘못된 역할");
    }

    return notices;
  }

  public List<NoticeDTO.ListAll> noticeList(List<Notice> notices) {
    List<NoticeDTO.ListAll> noticeList = new ArrayList<>();

    for (Notice notice : notices) {
      List<Image> images = imageRepository.findAllByNotice(notice).orElse(new ArrayList<>());
      List<String> urls = new ArrayList<>();
      for (Image image : images) {
        urls.add(image.getImageUrl());
      }

      NoticeDTO.ListAll dto = NoticeDTO.ListAll.builder()
              .noticeId(notice.getNoticeId())
              .create_date(notice.getCreatedDate())
              .content(notice.getContents())
              .imageUrl(urls)
              .writer_name(notice.getWriter().getName())
              .target_name(notice.getTarget().getName())
              .build();
      noticeList.add(dto);
    }

    return noticeList;
  }

  //알림장 상세보기
  public NoticeResponse getDetail(Long noticeId) throws Exception {
    Notice notice = noticeRepository.findById(noticeId)
            .orElseThrow(() -> new NoSuchElementException("알림장 찾기 실패 - 해당 알림장이 존재하지 않음"));

    List<Image> images = imageRepository.findAllByNotice(notice).orElse(new ArrayList<>());
    List<String> images_url = new ArrayList<>();
    for (Image image : images) {
      images_url.add(image.getImageUrl());
    }

    return NoticeResponse.builder()
            .notice_id(notice.getNoticeId())
      .writer_id(notice.getWriter().getId())
            .target_name(notice.getTarget().getName())
            .create_date(notice.getCreatedDate())
      .target_id(notice.getTarget().getId())
            .content(notice.getContents())
            .sub_content(notice.getSubContents())
            .image_url(images_url)
            .build();
  }

  public Long edit(NoticeDTO.Edit editDto, List<MultipartFile> files) throws Exception { // 알림장 수정: notice_id, writer_id, target_id, content, sub_content
    Notice notice = noticeRepository.findNoticeByNoticeId(editDto.getNotice_id())
            .orElseThrow(() -> new NoSuchElementException("알림장 찾기 실패 - 해당 알림장이 존재하지 않음"));

    NHResident writer = notice.getWriter();
    if (writer.getId() != editDto.getWriter_id() || writer.getUserRole() != UserRole.WORKER && writer.getUserRole() != UserRole.MANAGER)
      throw new UserAuthException("알림장 수정 실패 - 권한이 없는 사용자");

    NHResident target = nhResidentRepository.findById(editDto.getTarget_id())
            .orElseThrow(() -> new NHResidentException("입소자 찾기 실패 - target_id에 해당하는 입소자 없음"));

    // 기존 DB, S3에 저장된 이미지 삭제
    List<Image> deleteList = imageRepository.findAllByNotice(notice).orElse(new ArrayList<>());
    for (Image image : deleteList) {
      String url = image.getImageUrl();
      s3Service.delete(url.substring(59)); // S3에 저장된 기존 이미지 삭제
      imageRepository.deleteImageByImageId(image.getImageId()); // Image DB에 저장된 기존 이미지 삭제
    }

    // 수정된 이미지 넣기
    List<String> images_url = new ArrayList<>();
    if (files != null) {
      for (MultipartFile file : files) {
        if (!file.isEmpty()) {
          String url = URLDecoder.decode(s3Service.upload(file), "utf-8");
          Image image = Image.newNoticeImage(notice, url);
          images_url.add(url);
          imageRepository.save(image);
        }
      }
    }

    notice.editNotice(target, editDto.getContent(), editDto.getSub_content(), images_url);
    return notice.getNoticeId();
  }

  public void delete(Long notice_id) {
    Notice notice = noticeRepository.findById(notice_id).
            orElseThrow(() -> new NoSuchElementException("알림장 찾기 실패 - 해당 알림장이 존재하지 않음"));

    List<Image> images = imageRepository.findAllByNotice(notice).orElse(new ArrayList<>());
    for (Image image : images) {
      String image_url = image.getImageUrl();
      s3Service.delete(image_url.substring(59));
      imageRepository.deleteImageByImageId(image.getImageId());
    }

    Long deleted = noticeRepository.deleteNoticeByNoticeId(notice_id);
    if (deleted == 0)
      throw new InternalException("알림장 삭제 실패");
  }
}

