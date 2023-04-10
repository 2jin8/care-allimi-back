//package kr.ac.kumoh.allimi.service;//package kr.ac.kumoh.allimi.service;
//
//import kr.ac.kumoh.allimi.domain.Facility;
//import kr.ac.kumoh.allimi.domain.Notice;
//import kr.ac.kumoh.allimi.domain.NoticeContent;
//import kr.ac.kumoh.allimi.domain.User;
//import kr.ac.kumoh.allimi.dto.SignUpDTO;
//import kr.ac.kumoh.allimi.handler.UserDataHandler;
//import kr.ac.kumoh.allimi.handler.impl.UserDataHandlerImpl;
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
//@Import({UserDataHandler.class, UserService.class})
//public class UserServiceTest {
//
//    @MockBean
//    UserDataHandlerImpl userDataHandler;
//
//    @Autowired
//    UserService userService; //컨트롤러 관련 테스트가 아님. webmvcteat아님. test관련 객체 주입받아줘야한다.
//
//    @Test
//    void 회원가입Test() throws Exception {
//        //given
//
//        Mockito.when(userDataHandler.saveUser("asdf", "asdf", "주효림", "마우스", "01090333333"))
//                .thenReturn(User.newUser("asdf", "asdf", "주효림", "마우스", "01090333333"));
//
//        Long userId = userService.addUser(SignUpDTO.builder().id("asdf").password("asdf").protector_name("마우스").name("주효림")
//                .tel("01090333333").build());
////
////        Assertions.assertEquals(productDto.getProductId(), 123);
////        Assertions.assertEquals(productDto.getProductName(), "pen");
////        Assertions.assertEquals(productDto.getProductPrice(), 2000);
////        Assertions.assertEquals(productDto.getProductStock(), 3000);
//
//        verify(userDataHandler).saveUser("asdf", "asdf", "주효림", "마우스", "01090333333");
//    }
//
////    @Test
////    void getProductTest() {
////        //given 다른방식 구현
////        Mockito.when(noticeDataHandler.getNotice(123))
////                .thenReturn(new Notice(123, "pen", 2000, 3000));
////
////        ProductDto productDto = noticeService.getProduct(123);
////
////        Assertions.assertEquals(productDto.getProductId(), 123);
////        Assertions.assertEquals(productDto.getProductPrice(), 2000);
////
////        verify(noticeDataHandler).getNotice(123);
////    }
//
//
//}