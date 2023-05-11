package kr.ac.kumoh.allimi.service;

import kr.ac.kumoh.allimi.controller.response.AllNoticeResponse;
import kr.ac.kumoh.allimi.controller.response.NoticeResponse;
import kr.ac.kumoh.allimi.domain.*;
import kr.ac.kumoh.allimi.domain.func.AllNotice;
import kr.ac.kumoh.allimi.domain.func.Notice;
import kr.ac.kumoh.allimi.dto.allNotice.AllNoticeEditDto;
import kr.ac.kumoh.allimi.dto.allNotice.AllNoticeListDTO;
import kr.ac.kumoh.allimi.dto.allNotice.AllNoticeWriteDto;
import kr.ac.kumoh.allimi.dto.notice.NoticeEditDto;
import kr.ac.kumoh.allimi.exception.AllNoticeException;
import kr.ac.kumoh.allimi.exception.FacilityException;
import kr.ac.kumoh.allimi.exception.NHResidentException;
import kr.ac.kumoh.allimi.exception.NoticeException;
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
public class AllNoticeService {
  private final AllNoticeRepository allNoticeRepository;
  private final UserRepository userRepository;
  private final FacilityRepository facilityRepository;
  private final ImageRepository imageRepository;
  private final S3Service s3Service;

  public void write(AllNoticeWriteDto dto, List<MultipartFile> files) throws Exception {  // allnotice{user_id, facility_id, title, contents, important}, file{}
    User user = userRepository.findUserByUserId(dto.getUser_id())
            .orElseThrow(() -> new UserException("user not found"));

    UserRole userRole = userRepository.getUserRole(user.getCurrentNHResident(), user.getUserId())
            .orElseThrow(() -> new UserException("역할을 찾을 수 없습니다."));

    if (userRole != UserRole.MANAGER && userRole != UserRole.WORKER)
      throw new UserAuthException("권한이 없는 사용자입니다.");

    Facility facility = facilityRepository.findById(dto.getFacility_id())
            .orElseThrow(() -> new FacilityException("facility not found"));

    AllNotice allNotice = AllNotice.newAllNotice(user, facility, dto.getTitle(), dto.getContents(), dto.isImportant());

    List<Image> images = new ArrayList<>();
    if (files != null) {
      for (MultipartFile file : files) {
        if (!file.isEmpty()) {
          String url = URLDecoder.decode(s3Service.upload(file), "utf-8");
          Image image = Image.newAllNoticeImage(allNotice, url);
          images.add(image);
          imageRepository.save(image);
        }
      }
    }
    allNotice.addImages(images);
    AllNotice savedAllNotice = allNoticeRepository.save(allNotice);

    if (savedAllNotice == null)
      throw new NoticeException("전체공지 저장 실패");
  }

  //전체공지 목록보기
  public List<AllNoticeListDTO> allNoticeList(Long facilityId) throws Exception {
    Facility facility = facilityRepository.findById(facilityId)
            .orElseThrow(() -> new FacilityException("해당 시설이 없습니다"));

    List<AllNotice> allNoitces = allNoticeRepository.findByFacility(facility)
            .orElseGet(() -> new ArrayList());

    List<AllNoticeListDTO> dtos = new ArrayList<>();

    for (AllNotice allNotice : allNoitces) {
      List<Image> images = imageRepository.findAllByAllNotice(allNotice).orElse(new ArrayList<>());
      List<String> urls = new ArrayList<>();
      for (Image image : images) {
        urls.add(image.getImageUrl());
      }

      dtos.add(AllNoticeListDTO.builder()
              .allNoticeId(allNotice.getAllNoticeId())
              .create_date(allNotice.getCreateDate())
              .title(allNotice.getTitle())
              .content(allNotice.getContents())
              .important(allNotice.isImportant())
              .imageUrl(urls)
              .build());
    }

    return dtos;     // allNoticeId, create_date, title, content, important, imageUrl = new ArrayList<>();
  }

  public void edit(AllNoticeEditDto editDto, List<MultipartFile> files) throws Exception {
    AllNotice allNotice = allNoticeRepository.findById(editDto.getAllnotice_id())
            .orElseThrow(() -> new AllNoticeException("전체공지를 찾을 수 없습니다"));

    User user = userRepository.findUserByUserId(editDto.getUser_id())
            .orElseThrow(() -> new UserException("user not found"));

    UserRole userRole = userRepository.getUserRole(user.getCurrentNHResident(), user.getUserId())
            .orElseThrow(() -> new UserException("역할을 찾을 수 없습니다."));

    User writer = allNotice.getUser();

    if (writer.getUserId() != user.getUserId() || userRole != UserRole.MANAGER && userRole != UserRole.WORKER) {
      throw new UserAuthException("권한이 없는 사용자입니다.");
    }

    // 기존 DB, S3에 저장된 이미지 삭제
    List<Image> deleteList = imageRepository.findAllByAllNotice(allNotice).orElse(new ArrayList<>());
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
        Image image = Image.newAllNoticeImage(allNotice, url);
        images_url.add(url);
        imageRepository.save(image);
      }
    }

    allNotice.edit(editDto.getTitle(), editDto.getContents(), editDto.isImportant(), images_url);
  }

  // 전체 공지 삭제
  public Long delete(Long allnotice_id) {
    AllNotice allNotice = allNoticeRepository.findById(allnotice_id).
            orElseThrow(() -> new AllNoticeException("해당 전체공지가 없습니다"));

    List<Image> images = imageRepository.findAllByAllNotice(allNotice).orElse(new ArrayList<>());

    for (Image image : images) {
      String image_url = image.getImageUrl();
      s3Service.delete(image_url.substring(59));
      imageRepository.deleteImageByImageId(image.getImageId());
    }

    Long deleted = allNoticeRepository.deleteByAllNoticeId(allnotice_id);
    return deleted;
  }

//  // 전체공지 상세보기
//  public AllNoticeResponse getDetail(Long noticeId) throws Exception {
//    AllNotice allNotice = allNoticeRepository.findById(noticeId).orElseThrow(() -> new NoticeException("해당 알림장을 찾을 수 없습니다"));
//    User user = allNotice.getUser();
//
//    return NoticeResponse.builder()
//            .create_date(allNotice.getCreateDate())
//            .user_id(user.getUserId())
//            .notice_id(allNotice.getId())
//            .sub_content(allNotice.getSubContents())
//            .content(allNotice.getContents())afdasdfafas
//            .image_url(allNotice.getImageUrl())
//            .build();
//  }
}