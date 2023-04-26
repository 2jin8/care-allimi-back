package kr.ac.kumoh.allimi.repository;

import kr.ac.kumoh.allimi.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
