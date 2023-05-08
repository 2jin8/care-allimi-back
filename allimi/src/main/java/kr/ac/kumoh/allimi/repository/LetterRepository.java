package kr.ac.kumoh.allimi.repository;

import kr.ac.kumoh.allimi.domain.Facility;
import kr.ac.kumoh.allimi.domain.NHResident;
import kr.ac.kumoh.allimi.domain.User;
import kr.ac.kumoh.allimi.domain.func.Letter;
import kr.ac.kumoh.allimi.domain.func.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LetterRepository extends JpaRepository<Letter, Long> {

    @Query("select letter from Letter letter where letter.facility = ?1 order by letter.createDate desc")
    Optional<List> findAllByFacility(Facility facility);

    @Query("select letter from Letter letter where letter.nhResident = ?1 order by letter.createDate desc")
    Optional<List> findAllByNhResident(NHResident nhResident);

    Optional<Letter> findByLetterId(Long letterId);

  Long deleteLetterByLetterId(Long letterId);

//    Long deleteNoticeById(Long letterId);

//    void deleteByIdIn(List<Long> ids);
//    Optional<List> findByUserOrTarget(User user, NHResident target);
}
