package kr.ac.kumoh.allimi.handler.impl;//package kr.ac.kumoh.allimi.handler.impl;
//
//import kr.ac.kumoh.allimi.dao.NoticeDAO;
//import kr.ac.kumoh.allimi.domain.Facility;
//import kr.ac.kumoh.allimi.domain.Notice;
//import kr.ac.kumoh.allimi.domain.NoticeContent;
//import kr.ac.kumoh.allimi.domain.User;
//import kr.ac.kumoh.allimi.handler.NoticeDataHandler;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//@Service
//@Transactional
//@RequiredArgsConstructor
//public class NoticeDataHandlerImpl implements NoticeDataHandler {
//
//    private final NoticeDAO noticeDAO;
//
//    @Override
//    public Notice saveNotice(Facility facility, User user, User target, NoticeContent content) {
//        Notice productEntity = new Notice(facility, user, target, content);
//
//        return noticeDAO.saveNotice(productEntity);
//    }
//
//    @Override
//    public Notice getNotice(Long noticeId) {
//        return noticeDAO.getNotice(noticeId);
//    }
//}
