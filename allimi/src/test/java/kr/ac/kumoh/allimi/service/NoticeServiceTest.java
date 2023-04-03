//package kr.ac.kumoh.allimi.service;
//
//import kr.ac.kumoh.allimi.domain.Facility;
//import kr.ac.kumoh.allimi.domain.Notice;
//import kr.ac.kumoh.allimi.domain.NoticeContent;
//import kr.ac.kumoh.allimi.domain.User;
//import kr.ac.kumoh.allimi.dto.NoticeWriteDto;
//import kr.ac.kumoh.allimi.repository.NoticeRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//@ExtendWith(MockitoExtension.class)
//class NoticeServiceTest {
//
//    @InjectMocks
//    private NoticeService noticeService;
//
//    @Mock
//    private NoticeRepository noticeRepository;
//
//    @DisplayName("회원 가입")
//    @Test
//    void signUp() {
//        // given
//        SignUpRequest request = signUpRequest();
//        String encryptedPw = encoder.encode(request.getPw());
//
//        doReturn(new User(request.getEmail(), encryptedPw, UserRole.ROLE_USER)).when(userRepository)
//                .save(any(User.class));
//
//        // when
//        UserResponse user = userService.signUp(request);
//
//        // then
//        assertThat(user.getEmail()).isEqualTo(request.getEmail());
//        assertThat(encoder.matches(signUpDTO.getPw(), user.getPw())).isTrue();
//
//        // verify
//        verify(userRepository, times(1)).save(any(User.class));
//        verify(passwordEncoder, times(1)).encode(any(String.class));
//    }
//
//    private SignUpRequest signUpRequest() {
//        return SignUpRequest.builder()
//                .email("test@test.test")
//                .pw("test")
//                .build();
//    }
//
//    private UserResponse userResponse() {
//        return UserResponse.builder()
//                .email("test@test.test")
//                .pw("test")
//                .role(UserRole.ROLE_USER)
//                .build();
//    }
//}