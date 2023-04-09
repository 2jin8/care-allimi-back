//package kr.ac.kumoh.allimi.dao.impl;
//
//import kr.ac.kumoh.allimi.dao.NoticeDAO;
//import kr.ac.kumoh.allimi.domain.Notice;
//import kr.ac.kumoh.allimi.repository.NoticeRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class NoticeDAOImpl implements NoticeDAO {
//
//    private final NoticeRepository noticeRepository;
//
//    @Override
//    public Notice saveNotice(Notice notice) {
//        noticeRepository.save(notice);
//        return notice;
//    }
//
//    @Override
//    public Notice getNotice(Long noticeId){
//        Notice notice = noticeRepository.getReferenceById(noticeId);
//        return notice;
//    }
//}
