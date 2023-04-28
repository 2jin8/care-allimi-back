package kr.ac.kumoh.allimi.domain;

import jakarta.persistence.*;
import kr.ac.kumoh.allimi.domain.func.AllNotice;
import kr.ac.kumoh.allimi.domain.func.Notice;
import lombok.*;

@Entity
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long imageId;

    @Column(name = "image_url",length = 100000)
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "notice_id", referencedColumnName = "notice_id")
    private Notice notice;

    @ManyToOne
    @JoinColumn(name = "allnotice_id", referencedColumnName = "allnotice_id")
    private AllNotice allNotice;

    public static Image newNoticeImage(Notice notice, String imageUrl) {
        return new Image(null, imageUrl, notice, null);
    }

    public static Image newAllNoticeImage(AllNotice allnotice, String imageUrl) {
        return new Image(null, imageUrl, null, allnotice);
    }
}
