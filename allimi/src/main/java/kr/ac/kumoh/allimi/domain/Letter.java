package kr.ac.kumoh.allimi.domain;

import jakarta.persistence.*;

@Entity
public class Letter extends Functions {
    @Lob
    @Column(length = 100000)
    private String content;

    @Column(name = "is_read")
    private Boolean isRead;

}
