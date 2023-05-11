package kr.ac.kumoh.allimi.exception;

import kr.ac.kumoh.allimi.exception.user.UserAuthException;
import kr.ac.kumoh.allimi.exception.user.UserException;
import kr.ac.kumoh.allimi.exception.user.UserIdDuplicateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({UserException.class})
    protected ResponseEntity handleUserException(UserException e) {
        log.info("UserException = {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @ExceptionHandler({UserIdDuplicateException.class})
    protected ResponseEntity handleUserDuplicateException(UserIdDuplicateException e) {
        log.info("UserIdDuplicateException = {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @ExceptionHandler({UserAuthException.class})
    protected ResponseEntity handleUserAuthException(UserAuthException e) {
        log.info("UserAuthException = {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @ExceptionHandler({FacilityException.class})
    protected ResponseEntity handleFacilityException(FacilityException e) {
        log.info("FacilityException={}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @ExceptionHandler({AllNoticeException.class})
    protected ResponseEntity handleAllNoticeException(AllNoticeException e) {
        log.info("AllNoticeException={}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @ExceptionHandler({InvitationException.class})
    protected ResponseEntity handleInvitationException(InvitationException e) {
        log.info("InvitationException={}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @ExceptionHandler({LetterException.class})
    protected ResponseEntity handleLetterException(LetterException e) {
        log.info("LetterException={}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @ExceptionHandler({NHResidentException.class})
    protected ResponseEntity handleNHResidentException(NHResidentException e) {
        log.info("NHResidentException={}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @ExceptionHandler({NoticeException.class})
    protected ResponseEntity handleNoticeException(NoticeException e) {
        log.info("NoticeException={}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @ExceptionHandler({ScheduleException.class})
    protected ResponseEntity handleScheduleException(ScheduleException e) {
        log.info("ScheduleException={}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @ExceptionHandler({VisitException.class})
    protected ResponseEntity handleVisitException(VisitException e) {
        log.info("VisitException={}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
