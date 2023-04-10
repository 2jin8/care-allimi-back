//package kr.ac.kumoh.allimi.service;
//
//import kr.ac.kumoh.allimi.domain.Facility;
//import kr.ac.kumoh.allimi.domain.Notice;
//import kr.ac.kumoh.allimi.domain.NoticeContent;
//import kr.ac.kumoh.allimi.domain.User;
//import kr.ac.kumoh.allimi.handler.NoticeDataHandler;
//import kr.ac.kumoh.allimi.handler.impl.NoticeDataHandlerImpl;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.context.annotation.Import;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//import static org.mockito.Mockito.verify;
//
//// 내가 어느 객체 가져올지 모르겟다 -> @SpringBootTest(classes = {ProductDataHandlerImpl.class, ~~}) 매개변수가 없을 때만 전체 bean 로딩함.
//// 매개변수 넣어주면 이거대한 빈값 로딩 ㅇㅇ
//@ExtendWith(SpringExtension.class)  // 다른방식 ~ 필요한 내용만 갖다 쓴 거
//@Import({NoticeDataHandler.class, NoticeService.class})
//public class NoticeServiceTest {
//
//    @MockBean
//    NoticeDataHandlerImpl noticeDataHandler;
//
//    @Autowired
//    NoticeService noticeService; //컨트롤러 관련 테스트가 아님. webmvcteat아님. test관련 객체 주입받아줘야한다.
//
//    @Test
//    void saveProductTest() {
//        //given
//        Mockito.when(noticeDataHandler.saveNotice(new Facility(), new User(), new User(), new NoticeContent()))
//                .thenReturn(new Notice());
//        //NoticeContent.newNoticeContent(dto.getContents(), dto.getSubContents(), LocalDateTime.now());
//
//        ProductDto productDto = noticeService.write(123,"pen", 2000, 3000);
//
//        Assertions.assertEquals(productDto.getProductId(), 123);
//        Assertions.assertEquals(productDto.getProductName(), "pen");
//        Assertions.assertEquals(productDto.getProductPrice(), 2000);
//        Assertions.assertEquals(productDto.getProductStock(), 3000);
//
//        verify(noticeDataHandler).saveNotice(123, "pen", 2000, 3000);
//    }
//
//    @Test
//    void getProductTest() {
//        //given 다른방식 구현
//        Mockito.when(noticeDataHandler.getNotice(123))
//                .thenReturn(new Notice(123, "pen", 2000, 3000));
//
//        ProductDto productDto = noticeService.getProduct(123);
//
//        Assertions.assertEquals(productDto.getProductId(), 123);
//        Assertions.assertEquals(productDto.getProductPrice(), 2000);
//
//        verify(noticeDataHandler).getNotice(123);
//    }
//
//
//}