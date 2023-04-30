package kr.ac.kumoh.allimi.repository;

import kr.ac.kumoh.allimi.domain.Image;
import kr.ac.kumoh.allimi.domain.func.AllNotice;
import kr.ac.kumoh.allimi.domain.func.Notice;
import org.aspectj.weaver.ast.Not;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {

    Long deleteImageByImageId(Long imageId);

    Optional<List<Image>> findAllByNotice(Notice notice);
    Optional<List<Image>> findAllByAllNotice(AllNotice allNotice);

}
