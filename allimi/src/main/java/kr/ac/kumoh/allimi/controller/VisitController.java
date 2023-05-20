//package kr.ac.kumoh.allimi.controller;
//
//import jakarta.validation.Valid;
//import kr.ac.kumoh.allimi.controller.response.VisitResponse;
//import kr.ac.kumoh.allimi.dto.visit.*;
//import kr.ac.kumoh.allimi.exception.VisitException;
//import kr.ac.kumoh.allimi.exception.user.UserException;
//import kr.ac.kumoh.allimi.service.VisitService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Map;
//
//@Slf4j
//@RestController
//@RequiredArgsConstructor
//@CrossOrigin(origins = "*", allowedHeaders = "*")
//public class VisitController {
//    private final VisitService visitService;
//
//    @PostMapping("/visit")
//    public ResponseEntity write(@Valid @RequestBody VisitWriteDTO writeDTO) throws Exception { // 면회 신청
//        visitService.write(writeDTO);
//
//        return ResponseEntity.status(HttpStatus.OK).build();
//    }
//
//    @GetMapping("/v2/visit/{user_id}") // 면회신청 목록
//    public ResponseEntity visitList(@PathVariable("user_id") Long userId) throws Exception {
//        if (userId == null)
//            throw new UserException("VisitController 면회신청 목록: user_id가 null. 잘못된 입력");
//
//        List<VisitListDTO> visitList = visitService.visitList(userId);
//
//        return ResponseEntity.status(HttpStatus.OK).body(visitList);
//    }
//
//    @PatchMapping("/visit")
//    public ResponseEntity edit(@Valid @RequestBody VisitEditDTO editDTO) throws Exception { // 면회 수정
//        visitService.edit(editDTO);
//
//        return ResponseEntity.status(HttpStatus.OK).build();
//    }
//
//    @DeleteMapping("/visit")
//    public ResponseEntity delete(@RequestBody Map<String, Long> delete) throws Exception { // 면회 삭제
//        Long visitId = delete.get("visit_id");
//        if (visitId == null)
//            throw new VisitException("VisitController 면회 삭제: visit_id가 null. 잘못된 입력");
//
//        Long deleted = visitService.delete(visitId);
//        if (deleted == 0)
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//
//        return ResponseEntity.status(HttpStatus.OK).build();
//    }
//
//    @PostMapping("/visit/approval")
//    public ResponseEntity approval(@Valid @RequestBody VisitApprovalDTO approvalDTO) throws Exception { // 면회 승인 상태 변경
//        VisitResponse visitResponse = visitService.approval(approvalDTO); // WAITING -> REJECTED, APPROVED, COMPLETED
//
//        return ResponseEntity.status(HttpStatus.OK).body(visitResponse);
//    }
//}
//
