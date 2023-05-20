//package kr.ac.kumoh.allimi.service;
//
//import kr.ac.kumoh.allimi.domain.Facility;
//import kr.ac.kumoh.allimi.domain.NHResident;
//import kr.ac.kumoh.allimi.domain.User;
//import kr.ac.kumoh.allimi.domain.UserRole;
//import kr.ac.kumoh.allimi.domain.func.Letter;
//import kr.ac.kumoh.allimi.repository.LetterRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.lang.reflect.Field;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class LetterServiceTest {
//  @Mock
//  private LetterRepository letterRepository;
//
//  @InjectMocks
//  private LetterService letterService;
//
//  @BeforeEach
//  void setUp() {
//    MockitoAnnotations.initMocks(this);
//  }
//
//  @Test
//  void deleteEntity_Success() {
//    // 삭제할 엔티티의 ID
//    Long letterId = 1L;
//
//    // 삭제 시 호출되는 repository 메서드의 동작을 설정
//    when(letterRepository.deleteLetterByLetterId(letterId)).thenReturn(1L);
//
//    // 서비스의 삭제 메서드 호출
//    Long deleted = letterService.delete(letterId);
//
//    // 삭제 메서드 호출 시 repository의 delete 메서드가 호출되는지 확인
//    verify(letterRepository, times(1)).deleteLetterByLetterId(letterId);
//
//    // 삭제된 개수가 반환되는지 확인
//    assertEquals(1L, deleted);
//  }
//
//  @Test
//  void deleteEntity_NotFound() {
//    // 존재하지 않는 엔티티의 ID
//    Long letterId = 1L;
//
//    // 존재하지 않는 엔티티를 반환하도록 repository 메서드의 동작 설정
//    when(letterRepository.deleteLetterByLetterId(letterId)).thenReturn(0L);
//
//    // 서비스의 삭제 메서드 호출
//    Long deleted = letterService.delete(letterId);
//
//    // 삭제 메서드 호출 시 repository의 delete 메서드가 호출되지 않는지 확인
//    verify(letterRepository, never()).delete(any());
//  }
//}