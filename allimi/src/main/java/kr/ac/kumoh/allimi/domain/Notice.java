package kr.ac.kumoh.allimi.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@RequiredArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Notice {

    /**
     * notice_id
     * Target_id
     * User_id
     * Facility_id
     */

    @Id
    @Column(name = "notice_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long noticeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facility_id")
    private Facility facility;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", updatable = false)
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target")
    private User target;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="nc_id")
    private NoticeContent content;

    public static Notice newNotice(Facility facility, User user, User target, NoticeContent content) {
        Notice ntc = new Notice(null, facility, user, target, content);

        return ntc;
    }

    public void editNotice(User target, String contents, String subContents) {
        this.target = target;
        this.content.editNoticeContent(contents, subContents);
    }
}

