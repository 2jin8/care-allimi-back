package kr.ac.kumoh.allimi.domain;

import jakarta.persistence.*;

@Entity
public class AllNotice extends Functions {
    @Lob
    @Column(length = 100000)
    private String contents;

    @Column(name = "image_url", length = 1024)
    private String imageUrl;
}
