package kr.ac.kumoh.allimi.dao;//package kr.ac.kumoh.allimi.dao;

import kr.ac.kumoh.allimi.domain.Notice;
import kr.ac.kumoh.allimi.domain.User;

public interface UserDAO {

    User saveUser(User user);

    User getUser(Long user_id);

}
