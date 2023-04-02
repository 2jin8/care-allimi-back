package kr.ac.kumoh.allimi.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Entity
@Getter
@RequiredArgsConstructor
public class Notice {
    @Id
    @Column(name = "notice_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long noticeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facility_id")
    private Facility facility;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", updatable = false, insertable = false)
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User target;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="nc_id")
    private NoticeContent content;


    private Notice(Long noticeId, Facility facility, User user, User target, NoticeContent content) {
        this.noticeId = noticeId;
        this.facility = facility;
        this.user = user;
        this.target = target;
        this.content = content;
    }

    public static Notice newNotice(Facility facility, User user, User target, NoticeContent content) {
        Notice ntc = new Notice(null, facility, user, target, content);

        return ntc;
    }

    public Notice editNotice(Long noticeId, Facility facility, User user, User target, NoticeContent content) {
        Notice notice = new Notice(noticeId, facility, user, target, content);
        return notice;
    }
}

