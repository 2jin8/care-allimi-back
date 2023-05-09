package kr.ac.kumoh.allimi.repository;

import kr.ac.kumoh.allimi.domain.Facility;
import kr.ac.kumoh.allimi.domain.NHResident;
import kr.ac.kumoh.allimi.domain.func.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface VisitRepository extends JpaRepository<Visit, Long> {

  Long deleteVisitById(Long visitId);

  @Query("select visit from Visit visit where visit.facility = ?1")
  Optional<List> findAllByFacility(Facility facility);

  @Query("select visit from Visit visit where visit.nhResident = ?1 order by visit.createDate desc")
  Optional<List> findAllByTarget(NHResident target);
}
