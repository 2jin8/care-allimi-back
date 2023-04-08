package kr.ac.kumoh.allimi.handler;//package kr.ac.kumoh.allimi.handler;

import kr.ac.kumoh.allimi.domain.Facility;
import kr.ac.kumoh.allimi.domain.Notice;
import kr.ac.kumoh.allimi.domain.NoticeContent;
import kr.ac.kumoh.allimi.domain.User;

public interface UserDataHandler {
    public User saveUser(String id, String password, String name, String protector_name, String tel);

    public User getUser(Long noticeId);
}

//(Facility facility, User user, User target, NoticeContent content)

//public interface ProductDataHandler {
//  public ProductEntity saveProductEntity(long productId, String productName, int productPrice, int productStock);
//
//  public ProductEntity getProductEntity(long productId);
//}
