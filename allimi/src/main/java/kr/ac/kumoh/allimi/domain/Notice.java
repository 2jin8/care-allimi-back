package kr.ac.kumoh.allimi.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
public class Notice extends Functions {
    @Lob
    @Column(length = 100000)
    private String contents;
    @Lob
    @Column(name = "sub_content", length = 100000)
    private String subContents;

    @Column(name = "image_url", length = 1024)
    private String imageUrl;

    protected Notice(@NotNull User user, @NotNull NHResident nhResident, @NotNull Facility facility, String content, String subContent, String imageUrl) {
        setUser(user);
        setNhResident(nhResident);
        setFacility(facility);
        this.contents = content;
        this.subContents = subContent;
        this.imageUrl = imageUrl;
    }

    public static Notice newNotice(User user, NHResident target, Facility facility, String contents, String subContents, String imageUrl) {
        Notice ntc = new Notice(user, target, facility, contents, subContents, imageUrl);
        return ntc;
    }

    public void editNotice(NHResident target, String contents, String subContents, String imageUrl) {
        setNhResident(target);
        this.contents = contents;
        this.subContents = subContents;
        this.imageUrl = imageUrl;
    }
}
