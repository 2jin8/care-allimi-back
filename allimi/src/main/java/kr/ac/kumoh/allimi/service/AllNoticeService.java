package kr.ac.kumoh.allimi.service;

import kr.ac.kumoh.allimi.domain.*;
import kr.ac.kumoh.allimi.domain.func.AllNotice;
import kr.ac.kumoh.allimi.domain.func.Image;
import kr.ac.kumoh.allimi.dto.AllNoticeDTO;
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
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class AllNoticeService {
  private final AllNoticeRepository allNoticeRepository;
  private final NHResidentRepository nhResidentRepository;
  private final FacilityRepository facilityRepository;
  private final ImageRepository imageRepository;
  private final S3Service s3Service;

  // 공지사항 작성
  public void write(AllNoticeDTO.Write dto, List<MultipartFile> files) throws Exception {
    NHResident writer = nhResidentRepository.findById(dto.getWriter_id())
            .orElseThrow(() -> new NHResidentException("입소자 찾기 실패 - writer_id에 해당하는 입소자 없음"));

    if (writer.getUserRole() != UserRole.WORKER && writer.getUserRole() != UserRole.MANAGER)
      throw new UserAuthException("공지사항 작성 실패 - 권한이 없는 사용자");

    AllNotice allNotice = AllNotice.newAllNotice(writer, dto.getTitle(), dto.getContents(), dto.isImportant());

    List<Image> images = new ArrayList<>();
    if (files != null) {
      if (files.size() > 10)
        throw new FileCountExceedException("사진의 최대 업로드 수는 10장입니다.");

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
      throw new InternalException("공지사항 저장 실패");
  }

  // 공지사항 목록보기
  public List<AllNoticeDTO.ListAll> allNoticeList(Long facilityId) throws Exception {
    Facility facility = facilityRepository.findById(facilityId)
            .orElseThrow(() -> new FacilityException("시설 찾기 실패 - 해당 시설이 존재하지 않음"));

    List<NHResident> nhResidents = nhResidentRepository.findWorkerAndManagerByFacilityId(facilityId)
            .orElseThrow(() -> new NHResidentException("입소자 찾기 실패 - 해당하는 입소자 없음"));

    List<AllNotice> allNoitces = new ArrayList<>();
    for (NHResident nhResident : nhResidents) {
      allNoitces.addAll(allNoticeRepository.findAllByWriter(nhResident).orElse(new ArrayList<>()));
    }

    allNoitces = allNoitces.stream().sorted(Comparator.comparing(AllNotice::getCreatedDate).reversed()).collect(Collectors.toList());

    List<AllNoticeDTO.ListAll> dtos = new ArrayList<>();
    for (AllNotice allNotice : allNoitces) {
      List<Image> images = imageRepository.findAllByAllNotice(allNotice).orElse(new ArrayList<>());
      List<String> urls = new ArrayList<>();
      for (Image image : images) {
        urls.add(image.getImageUrl());
      }

      dtos.add(AllNoticeDTO.ListAll.builder()
              .allNoticeId(allNotice.getAllNoticeId())
              .create_date(allNotice.getCreatedDate())
              .title(allNotice.getTitle())
          .writer_id(allNotice.getWriter().getId())
              .content(allNotice.getContents())
              .important(allNotice.isImportant())
              .imageUrl(urls).build());
    }

    return dtos;     // allNoticeId, create_date, title, content, important, imageUrl = new ArrayList<>();
  }

  // 공지사항 수정
  public void edit(AllNoticeDTO.Edit editDto, List<MultipartFile> files) throws Exception {
    AllNotice allNotice = allNoticeRepository.findById(editDto.getAllnotice_id())
            .orElseThrow(() -> new AllNoticeException("공지사항 찾기 실패 - 해당 공지사항이 존재하지 않음"));

    NHResident writer = allNotice.getWriter();
    NHResident editer = nhResidentRepository.findById(editDto.getWriter_id())
            .orElseThrow(() -> new NHResidentException("입소자 찾기 실패 - writer_id에 해당하는 입소자 없음"));

    if (writer.getId() != editer.getId() || editer.getUserRole() != UserRole.WORKER && editer.getUserRole() != UserRole.MANAGER)
      throw new UserAuthException("공지사항 수정 실패 - 권한이 없는 사용자");

    // 기존 DB, S3에 저장된 이미지 삭제
    List<Image> deleteList = imageRepository.findAllByAllNotice(allNotice).orElse(new ArrayList<>());
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
          Image image = Image.newAllNoticeImage(allNotice, url);
          images_url.add(url);
          imageRepository.save(image);
        }
      }
    }

    allNotice.edit(editDto.getTitle(), editDto.getContents(), editDto.isImportant(), images_url);
  }

  // 공지사항 삭제
  public void delete(Long allnotice_id) {
    AllNotice allNotice = allNoticeRepository.findById(allnotice_id).
            orElseThrow(() -> new AllNoticeException("공지사항 찾기 실패 - 해당 공지사항이 존재하지 않음"));

    List<Image> images = imageRepository.findAllByAllNotice(allNotice).orElse(new ArrayList<>());
    for (Image image : images) {
      String image_url = image.getImageUrl();
      s3Service.delete(image_url.substring(59));
      imageRepository.deleteImageByImageId(image.getImageId());
    }

    Long deleted = allNoticeRepository.deleteByAllNoticeId(allnotice_id);
    if (deleted == 0)
      throw new InternalException("공지사항 삭제 실패");
  }

}