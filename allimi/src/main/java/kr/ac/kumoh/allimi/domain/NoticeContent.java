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
    @Column(columnDefinition = "TEXT CHARACTER SET UTF8")
    private String contents;
    //columnDefinition = "VARCHAR(255) CHARACTER SET UTF8"
    @Lob
    @Column(name = "sub_content", columnDefinition = "TEXT CHARACTER SET UTF8")
    private String subContents;

    @CreationTimestamp
    @Column(name = "create_date")
    private LocalDateTime createDate;

    public static NoticeContent newNoticeContent(String contents, String subContents, LocalDateTime createDate) {
        //json으로_변경할_스트링.replace("\n", "\\r\\n"); 나중에 안되면 ㄱ
//        System.out.println("@@@@@" + contents);
//        String replaceContents = contents.replace("\n", "\\r\\n");
//        System.out.println("$$$$$" + contents);

        NoticeContent nc = new NoticeContent(null, contents, subContents, createDate);

        return nc;
    }

    public void editNoticeContent(String contents, String subContents) {

        this.contents = contents;
        this.subContents = subContents;

//        NoticeContent noticeContent = new NoticeContent(contents, subContents, this.getCreateDate());
//        return noticeContent;
    }

    public static NoticeContent newNoticeContent(String contents, String subContents) {
        NoticeContent nc = new NoticeContent(null, contents, subContents, null);
        return nc;
    }

}



//Lob 관련 LOB lob https://kogle.tistory.com/250