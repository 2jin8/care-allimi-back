package kr.ac.kumoh.allimi.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import kr.ac.kumoh.allimi.domain.Facility;
import kr.ac.kumoh.allimi.dto.schedule.ScheduleDeleteDTO;
import kr.ac.kumoh.allimi.dto.schedule.ScheduleEditDTO;
import kr.ac.kumoh.allimi.dto.schedule.ScheduleListDTO;
import kr.ac.kumoh.allimi.dto.schedule.ScheduleWriteDTO;
import kr.ac.kumoh.allimi.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v4")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ScheduleController {
  private final ScheduleService scheduleService;

  //일정 등록
  @PostMapping(value = "/schedule")
  public ResponseEntity write(@Valid @RequestBody ScheduleWriteDTO writeDTO) throws Exception {
    Long scheduleId = scheduleService.write(writeDTO);

    Map<String, Long> map = new HashMap<>();
    map.put("schedule_id", scheduleId);

    return ResponseEntity.status(HttpStatus.OK).body(map);
  }

  //일정 수정
  @PatchMapping(value = "/schedule")
  public ResponseEntity edit(@Valid @RequestBody ScheduleEditDTO editDTO) throws Exception { //schedule_id, date, texts;
    scheduleService.edit(editDTO);

    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @DeleteMapping(value = "/schedule")
  public ResponseEntity delete(@Valid @RequestBody ScheduleDeleteDTO deleteDTO) throws Exception { // schedule_id, user_id;
    scheduleService.delete(deleteDTO);

    return ResponseEntity.status(HttpStatus.OK).build();
  }

  // 잘 안쓸듯. 관리자만 쓸거같다
  @GetMapping(value = "/schedule/{facility_id}")
  public ResponseEntity list(@PathVariable Long facility_id) throws Exception {
    List<ScheduleListDTO> scheduleList = scheduleService.scheduleList(facility_id);

    return ResponseEntity.status(HttpStatus.OK).body(scheduleList);
  }

  @GetMapping(value = "/schedule/{facility_id}/{year_month}") //2023-05
  public ResponseEntity monthList(@PathVariable("facility_id") Long facilityId, @PathVariable("year_month") String yearMonth) throws Exception {
    List<ScheduleListDTO> scheduleList;

    scheduleList = scheduleService.monthlyList(facilityId, yearMonth);

    return ResponseEntity.status(HttpStatus.OK).body(scheduleList);
  }
}
