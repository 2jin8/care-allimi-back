package kr.ac.kumoh.allimi.service;

import kr.ac.kumoh.allimi.domain.Facility;
import kr.ac.kumoh.allimi.domain.Notice;
import kr.ac.kumoh.allimi.domain.NoticeContent;
import kr.ac.kumoh.allimi.domain.User;
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

        User targetUser = userRepository.findByUserId(dto.getTarget())
                .orElseThrow(() -> new UserException("target user not found"));

        User user = userRepository.findByUserId(dto.getUserId())
                .orElseThrow(() -> new UserException("user not found"));

        Facility facility = facilityRepository.findById(dto.getFacilityId())
                .orElseThrow(() -> new UserException("user not found"));

        System.out.println(content.getContents());

        Notice notice = Notice.newNotice(facility, user, targetUser, content);


        return noticeRepository.save(notice);
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
