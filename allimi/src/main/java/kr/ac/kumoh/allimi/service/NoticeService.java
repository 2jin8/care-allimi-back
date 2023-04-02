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
                    .noticeId(notice.getNoticeId())
                    .create_date(noticeContent.getCreateDate())
                    .content(noticeContent.getContents()).build();

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
                .orElseThrow(() -> new UserException("user not found"));

        Notice notice = Notice.newNotice(facility, user, targetUser, content);


        return noticeRepository.save(notice);
    }

    public Notice edit(NoticeEditDto editDto) {
        NoticeContent noticeContent = new NoticeContent().editNoticeContent(editDto.getNcId(), editDto.getContent(), editDto.getSubContent());

        Facility facility = facilityRepository.findById(editDto.getFacilityId())
                .orElseThrow(() -> new UserException("facility not found"));

        User user = userRepository.findUserByUserId(editDto.getUserId())
                .orElseThrow(() -> new UserException("user not found"));

        User target = userRepository.findUserByUserId(editDto.getTargetId())
                .orElseThrow(() -> new UserException("target not found"));

        Notice checkNotice = noticeRepository.findById(editDto.getNoticeId())
                .orElseGet(() -> null);

//        if (checkNotice.isEmpty()) {
//            return null;
//        }

        noticeRepository.findById(editDto.getNoticeId())
                .orElseThrow(() -> new UserException("notice not found"));

        Notice notice = new Notice().editNotice(editDto.getNoticeId(), facility, user, target, noticeContent);
        return noticeRepository.save(notice);
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
                    .noticeId(notice.getNoticeId())
                    .create_date(noticeContent.getCreateDate())
                    .content(noticeContent.getContents()).build();

            userNoticeResponsLists.add(nr);
        }

        return userNoticeResponsLists;
    }

    public NoticeResponse findNotice(Long noticeId) {

        Notice notice = noticeRepository.findByNoticeId(noticeId)
                .orElseThrow(() -> new NoticeException("해당 게시글이 없습니다"));

        NoticeContent nContent = notice.getContent();

        return NoticeResponse.builder().create_date(nContent.getCreateDate())
                .noticeId(notice.getNoticeId())
                .subContent(nContent.getSubContents())
                .content(nContent.getContents())
                .build();
    }

    //    private Long noticeId;
    //    private Facility facility;
    //    private User user;
    //    private User target;
    //    private NoticeContent content;


    //    private Long user_id;
//    private Long target;
//    private String contents;
//    private String sub_contents;
//    private LocalDateTime create_date;

}
