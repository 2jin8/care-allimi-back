package kr.ac.kumoh.allimi.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
public class Visit extends Functions{

    @Column(name = "is_read")
    private Boolean isRead;

    @Column(length = 512)
    private String texts;

    @Column(name = "want_date")
    @NotNull
    private LocalDateTime wantDate;

    private String tel;

    @NotNull
    @Column(name = "visitor_name")
    private String visitorName;

    @Column(name = "rej_reason")
    private String rejReson;

    @Enumerated(EnumType.STRING) //대기중, 거절, 승인, 완료
    private VisitState state = VisitState.WAITING;
}
