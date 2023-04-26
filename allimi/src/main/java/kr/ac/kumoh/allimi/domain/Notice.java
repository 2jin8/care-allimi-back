package kr.ac.kumoh.allimi.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "imageId")
    @Column(name = "image_id", length = 100000)
    private List<Image> images = new ArrayList<>();

    protected Notice(@NotNull User user, @NotNull NHResident nhResident, @NotNull Facility facility, String content, String subContent, List<Image> images) {
        setUser(user);
        setNhResident(nhResident);
        setFacility(facility);

        this.contents = content;
        this.subContents = subContent;
        this.images = images;
    }

    public static Notice newNotice(User user, NHResident target, Facility facility, String contents, String subContents) {
        Notice ntc = new Notice(user, target, facility, contents, subContents, null);
        return ntc;
    }

    public void addImages(List<Image> images) {
        this.images = images;
    }

//    public void editNotice(NHResident target, String contents, String subContents, String[] imageUrls) {
//        setNhResident(target);
//        this.contents = contents;
//        this.subContents = subContents;
//
//        this.image = null;
//        for(String url: imageUrls) {
//            Image i = Image.newNoticeImage(this, url);
//            this.image.add(i);
//        }
//    }
}
