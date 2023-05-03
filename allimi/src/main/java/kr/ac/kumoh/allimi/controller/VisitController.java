package kr.ac.kumoh.allimi.controller;

import kr.ac.kumoh.allimi.controller.response.VisitResponse;
import kr.ac.kumoh.allimi.dto.visit.VisitApprovalDTO;
import kr.ac.kumoh.allimi.dto.visit.VisitDeleteDTO;
import kr.ac.kumoh.allimi.dto.visit.VisitEditDTO;
import kr.ac.kumoh.allimi.dto.visit.VisitWriteDTO;
import kr.ac.kumoh.allimi.exception.FacilityException;
import kr.ac.kumoh.allimi.exception.NHResidentException;
import kr.ac.kumoh.allimi.exception.VisitException;
import kr.ac.kumoh.allimi.exception.user.UserException;
import kr.ac.kumoh.allimi.service.VisitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class VisitController {
    private final VisitService visitService;

    @PostMapping("/visit")
    public ResponseEntity write(@RequestBody VisitWriteDTO writeDTO) { // 면회 신청
        try {
            visitService.write(writeDTO);
        } catch (UserException | FacilityException | NHResidentException e) {
            log.info("VisitController 면회 신청: 사용자 | 시설 | 입소자 잘못 입력");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            log.info("{}", e);
            log.info("VisitController 면회 신청: 기타 예외");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping("/visit")
    public ResponseEntity edit(@RequestBody VisitEditDTO editDTO) { // 면회 수정
        try {
            visitService.edit(editDTO);
        } catch (UserException | NHResidentException | VisitException e) {
            log.info("VisitController 면회 수정: 사용자 | 시설 | 입소자 잘못 입력");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            log.info("VisitController 면회 수정: 기타 예외");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/visit")
    public ResponseEntity delete(@RequestBody VisitDeleteDTO deleteDTO) { // 면회 삭제
        Long visitId = deleteDTO.getVisit_id();
        if (visitId == null) {
            log.info("VisitController 면회 삭제: 면회 id가 null로 들어옴");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        try {
            Long deleted = visitService.delete(visitId);
            if (deleted == 0) {
                log.info("VisitController 면회 삭제: 제대로 삭제되지 않음");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (VisitException e) {
            log.info("VisitController 면회 삭제: 해당 면회가 존재하지 않음");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            log.info("VisitController 면회 삭제: 기타 예외");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/visit/approval")
    public ResponseEntity approval(@RequestBody VisitApprovalDTO approvalDTO) { // 면회 승인 상태 변경
        // WAITING -> REJECTED, APPROVED, COMPLETED
        VisitResponse visitResponse;
        try {
            visitResponse = visitService.approval(approvalDTO);
        } catch (VisitException e) {
            log.info("VisitController 면회 승인 상태 변경: 해당 면회가 존재하지 않음");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            log.info("VisitController 면회 승인 상태 변경: 기타 예외");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(visitResponse);
    }
}

