package kr.ac.kumoh.allimi.service;

import kr.ac.kumoh.allimi.domain.*;
import kr.ac.kumoh.allimi.dto.NoticeEditDto;
import kr.ac.kumoh.allimi.dto.NoticeResponse;
import kr.ac.kumoh.allimi.dto.NoticeWriteDto;
import kr.ac.kumoh.allimi.exception.NoticeException;
import kr.ac.kumoh.allimi.exception.UserException;
import kr.ac.kumoh.allimi.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class NoticeService {

    @Autowired
    private NoticeRepository noticeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FacilityRepository facilityRepository;

    // List<Notice> notices = noticeService.noticeList();
    public List<NoticeResponse> noticeList() {
        List<Notice> notices = noticeRepository.findAll();

        List<NoticeResponse> noticeListRespons = new ArrayList();

        for (Notice notice : notices) {
            NoticeContent noticeContent = notice.getContent();

            NoticeResponse nr = NoticeResponse.builder()
                    .noticeId(notice.getId())
                    .create_date(noticeContent.getCreateDate())
                    .content(noticeContent.getContent())
                    .subContent(noticeContent.getSubContents())
                    .build();

            noticeListRespons.add(nr);
        }

        return noticeListRespons;
    }

    public Notice write(NoticeWriteDto dto) {

        NoticeContent content = NoticeContent.newNoticeContent(dto.getContents(), dto.getSubContents(), LocalDateTime.now());

        User targetUser = userRepository.findUserByUserId(dto.getTarget())
                .orElseThrow(() -> new UserException("target user not found"));

        User user = userRepository.findUserByUserId(dto.getUserId())
                .orElseThrow(() -> new UserException("user not found"));

        Facility facility = facilityRepository.findById(dto.getFacilityId())
                .orElseThrow(() -> new UserException("facility not found"));

        Notice notice = Notice.newNotice(facility, user, targetUser, content);

        return noticeRepository.save(notice);
    }

    public void edit(NoticeEditDto editDto) {
        Notice checkNotice = noticeRepository.findById(editDto.getNoticeId())
                .orElseThrow(() -> new NoticeException("해당 notice가 없습니다"));

        User writer = checkNotice.getUser();

        User user = userRepository.findUserByUserId(editDto.getUserId())
                .orElseThrow(() -> new UserException("사용자를 찾을 수 없습니다"));

        if (user == null) {
            throw new UserException("권한이 없는 사용자 입니다");
        }

        if (writer.getUserId() != editDto.getUserId() && user.getUserRole() != UserRole.MANAGER) {
            throw new UserException("권한이 없는 사용자 입니다");
        }

        User targetUser = userRepository.findUserByUserId(editDto.getTargetId())
                .orElseThrow(() -> new UserException("target을 찾을 수 없습니다"));

        checkNotice.editNotice(targetUser, editDto.getContent(), editDto.getSubContent());
    }

    public Long delete(Long notice_id) {
        Long deleted = noticeRepository.deleteNoticeByNoticeId(notice_id);
        return deleted;
    }

    public List<NoticeResponse> userNoticeList(Long userId) {

        User user = userRepository.findUserByUserId(userId)
                .orElseThrow(() -> new UserException("user가 없습니다"));

        List<Notice> userNotice = noticeRepository.findByTarget(user)
                .orElseGet(() -> new ArrayList<Notice>());

        List<NoticeResponse> userNoticeResponsLists = new ArrayList<>();

        for (Notice notice : userNotice) {
            NoticeContent noticeContent = notice.getContent();

            NoticeResponse nr = NoticeResponse.builder()
                    .noticeId(notice.getId())
                    .create_date(noticeContent.getCreateDate())
                    .content(noticeContent.getContent())
                    .subContent(noticeContent.getSubContents())
                    .build();

            userNoticeResponsLists.add(nr);
        }

        return userNoticeResponsLists;
    }

    public NoticeResponse findNotice(Long noticeId) {

        Notice notice = noticeRepository.findByNoticeId(noticeId)
                .orElseThrow(() -> new NoticeException("해당 게시글이 없습니다"));

        NoticeContent nContent = notice.getContent();

        return NoticeResponse.builder().create_date(nContent.getCreateDate())
                .noticeId(notice.getId())
                .subContent(nContent.getSubContents())
                .content(nContent.getContent())
                .build();
    }

}
