package kr.ac.kumoh.allimi.service;

import kr.ac.kumoh.allimi.domain.Facility;
import kr.ac.kumoh.allimi.domain.Notice;
import kr.ac.kumoh.allimi.domain.NoticeContent;
import kr.ac.kumoh.allimi.domain.User;
import kr.ac.kumoh.allimi.dto.NoticeEditDto;
import kr.ac.kumoh.allimi.dto.NoticeResponse;
import kr.ac.kumoh.allimi.dto.NoticeWriteDto;
import kr.ac.kumoh.allimi.exception.UserException;
import kr.ac.kumoh.allimi.repository.FacilityRepository;
import kr.ac.kumoh.allimi.repository.NoticeRepository;
import kr.ac.kumoh.allimi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

        List<NoticeResponse> noticeResponses = new ArrayList();

        for (Notice notice : notices) {
            NoticeContent noticeContent = notice.getContent();
            noticeResponses.add(new NoticeResponse(noticeContent.getCreateDate(), noticeContent.getContents()));
            System.out.println(notice.getContent().getCreateDate());
        }

        return noticeResponses;
    }

    public Notice write(NoticeWriteDto dto) {

        NoticeContent content = NoticeContent.newNoticeContent(dto.getContents(), dto.getSubContents(), dto.getCreateDate());

        User targetUser = userRepository.findUserByUserId(dto.getTarget())
                .orElseThrow(() -> new UserException("target user not found"));

        User user = userRepository.findUserByUserId(dto.getUserId())
                .orElseThrow(() -> new UserException("user not found"));

        Facility facility = facilityRepository.findById(dto.getFacilityId())
                .orElseThrow(() -> new UserException("user not found"));

        System.out.println(content.getContents());

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

        Optional<Notice> checkNotice = noticeRepository.findById(editDto.getNoticeId());
        if (checkNotice.isEmpty()) {
            return null;
        }

        noticeRepository.findById(editDto.getNoticeId())
                .orElseThrow(() -> new UserException("notice not found"));

        Notice notice = new Notice().editNotice(editDto.getNoticeId(), facility, user, target, noticeContent);
        return noticeRepository.save(notice);
    }

    public Long delete(Long notice_id) {
//        List<Notice> notices = noticeRepository.deleteNoticeByNoticeId(notice_id);
        Long deleted = noticeRepository.deleteNoticeByNoticeId(notice_id);
        return deleted;
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
