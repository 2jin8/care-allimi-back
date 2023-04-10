package kr.ac.kumoh.allimi.handler.impl;//package kr.ac.kumoh.allimi.handler.impl;
import kr.ac.kumoh.allimi.dao.UserDAO;
import kr.ac.kumoh.allimi.domain.Facility;
import kr.ac.kumoh.allimi.domain.Notice;
import kr.ac.kumoh.allimi.domain.NoticeContent;
import kr.ac.kumoh.allimi.domain.User;
import kr.ac.kumoh.allimi.handler.UserDataHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserDataHandlerImpl implements UserDataHandler {

    private final UserDAO userDAO;

    @Override
    public User saveUser(String id, String password, String name, String protector_name, String tel) {
        User user = User.newUser(id, password, name, tel);

        return userDAO.saveUser(user);
    }

    @Override
    public User getUser(Long noticeId) {
        return userDAO.getUser(noticeId);
    }
}
