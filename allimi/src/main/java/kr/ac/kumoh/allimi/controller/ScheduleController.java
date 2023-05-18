package kr.ac.kumoh.allimi.controller;

import jakarta.validation.Valid;
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

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v2")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ScheduleController {
    private final ScheduleService scheduleService;

    @PostMapping(value = "/schedule")
    public ResponseEntity write(@Valid @RequestBody ScheduleWriteDTO writeDTO) throws Exception {
        scheduleService.write(writeDTO);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping(value = "/schedule")
    public ResponseEntity edit(@Valid @RequestBody ScheduleEditDTO editDTO) throws Exception {
        scheduleService.edit(editDTO);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping(value = "/schedule")
    public ResponseEntity delete(@Valid @RequestBody ScheduleDeleteDTO deleteDTO) throws Exception {
        scheduleService.delete(deleteDTO);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping(value = "/schedule/{facility_id}")
    public ResponseEntity list(@PathVariable Long facility_id) throws Exception {
        List<ScheduleListDTO> scheduleList = scheduleService.scheduleList(facility_id);

        return ResponseEntity.status(HttpStatus.OK).body(scheduleList);
    }

    @GetMapping(value = "/schedule/{facility_id}/{year_month}") //2023-05
    public ResponseEntity monthList(@PathVariable("facility_id") Long facilityId, @PathVariable("year_month") String yearMonth) {
        List<ScheduleListDTO> scheduleList;

        scheduleList = scheduleService.monthlyList(facilityId, yearMonth);

        return ResponseEntity.status(HttpStatus.OK).body(scheduleList);
    }
}
