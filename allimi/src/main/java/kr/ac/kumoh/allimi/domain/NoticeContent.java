package kr.ac.kumoh.allimi.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class NoticeContent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nc_id")
    private Long ncId;

    @Lob
    @Column(columnDefinition = "VARCHAR(255) CHARACTER SET UTF8")
    private String contents;
//columnDefinition = "VARCHAR(255) CHARACTER SET UTF8"
    @Lob
    @Column(name = "sub_content", columnDefinition = "VARCHAR(255) CHARACTER SET UTF8")
    private String subContents;

    @CreationTimestamp
    @Column(name = "create_date")
    private LocalDateTime createDate;


    public static NoticeContent newNoticeContent(String contents, String subContents, LocalDateTime createDate) {
        NoticeContent nc = new NoticeContent(null, contents, subContents, createDate);

        return nc;
    }

}



//Lob 관련 LOB lob https://kogle.tistory.com/250
