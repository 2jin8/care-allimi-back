package kr.ac.kumoh.allimi.controller;

import kr.ac.kumoh.allimi.dto.schedule.ScheduleDeleteDTO;
import kr.ac.kumoh.allimi.dto.schedule.ScheduleEditDTO;
import kr.ac.kumoh.allimi.dto.schedule.ScheduleListDTO;
import kr.ac.kumoh.allimi.dto.schedule.ScheduleWriteDTO;
import kr.ac.kumoh.allimi.exception.FacilityException;
import kr.ac.kumoh.allimi.exception.ScheduleException;
import kr.ac.kumoh.allimi.exception.user.UserAuthException;
import kr.ac.kumoh.allimi.exception.user.UserException;
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
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping(value = "/v2/schedule")
    public ResponseEntity write(@RequestBody ScheduleWriteDTO writeDTO) {

        try {
            scheduleService.write(writeDTO);
        } catch (UserException | FacilityException e) {
            log.info("ScheduleController 일정표 작성: 사용자 또는 시설 에러");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (UserAuthException e) {
            log.info("ScheduleController 일정표 작성: 작성할 권한 없음");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (ScheduleException e) {
            log.info("ScheduleController 일정표 작성: 일정표 저장 에러");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            log.info("ScheduleController 일정표 작성: 기타 에러");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping(value = "/v2/schedule")
    public ResponseEntity edit(@RequestBody ScheduleEditDTO editDTO) {

        try {
            scheduleService.edit(editDTO);
        } catch (ScheduleException | UserException e) {
            log.info("ScheduleController 일정표 수정: 사용자 또는 일정 에러");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (UserAuthException e) {
            log.info("ScheduleController 일정표 수정: 수정할 권한 없음");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            log.info("ScheduleController 일정표 수정: 기타 에러");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping(value = "/v2/schedule")
    public ResponseEntity delete(@RequestBody ScheduleDeleteDTO deleteDTO) {

        try {
            scheduleService.delete(deleteDTO);
        } catch (ScheduleException | UserException e) {
            log.info("ScheduleController 일정표 삭제: 사용자 또는 일정 에러");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (UserAuthException e) {
            log.info("ScheduleController 일정표 삭제: 삭제할 권한 없음");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            log.info("ScheduleController 일정표 삭제: 기타 에러");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping(value = "/v2/schedule/{facility_id}")
    public ResponseEntity list(@PathVariable Long facility_id) {
        List<ScheduleListDTO> scheduleList;

        try {
            scheduleList = scheduleService.scheduleList(facility_id);
        } catch (FacilityException e) {
            log.info("ScheduleController 일정표 목록: 잘못된 시설 정보");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            log.info("ScheduleController 일정표 목록: 기타 에러");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(scheduleList);
    }
}
