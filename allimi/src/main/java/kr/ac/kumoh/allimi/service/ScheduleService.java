package kr.ac.kumoh.allimi.service;


import kr.ac.kumoh.allimi.domain.Facility;
import kr.ac.kumoh.allimi.domain.func.Schedule;
import kr.ac.kumoh.allimi.domain.User;
import kr.ac.kumoh.allimi.domain.UserRole;
import kr.ac.kumoh.allimi.dto.schedule.ScheduleDeleteDTO;
import kr.ac.kumoh.allimi.dto.schedule.ScheduleEditDTO;
import kr.ac.kumoh.allimi.dto.schedule.ScheduleListDTO;
import kr.ac.kumoh.allimi.dto.schedule.ScheduleWriteDTO;
import kr.ac.kumoh.allimi.exception.FacilityException;
import kr.ac.kumoh.allimi.exception.ScheduleException;
import kr.ac.kumoh.allimi.exception.user.UserAuthException;
import kr.ac.kumoh.allimi.exception.user.UserException;
import kr.ac.kumoh.allimi.repository.FacilityRepository;
import kr.ac.kumoh.allimi.repository.ScheduleRepository;
import kr.ac.kumoh.allimi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;
    private final FacilityRepository facilityRepository;

    public void write(ScheduleWriteDTO writeDTO) throws Exception {
        User user = userRepository.findUserByUserId(writeDTO.getUser_id())
                .orElseThrow(() -> new UserException("사용자를 찾을 수 없습니다."));

        Facility facility = facilityRepository.findById(writeDTO.getFacility_id())
                .orElseThrow(() -> new FacilityException("시설을 찾을 수 없습니다."));

        UserRole userRole = userRepository.getUserRole(user.getCurrentNHResident(), user.getUserId())
                .orElseThrow(() -> new UserException("역할을 찾을 수 없습니다."));

        if (userRole == UserRole.PROTECTOR)
            throw new UserAuthException("권한이 없는 사용자입니다.");

        Schedule schedule = Schedule.newSchedule(user, facility, writeDTO.getDate(), writeDTO.getTexts());

        Schedule saved = scheduleRepository.save(schedule);
        if (saved == null)
            throw new ScheduleException("일정 저장 실패");
    }

    public void edit(ScheduleEditDTO editDTO) throws Exception {

        Schedule schedule = scheduleRepository.findById(editDTO.getSchedule_id())
                .orElseThrow(() -> new ScheduleException("해당 일정을 찾을 수 없습니다."));

        User user = userRepository.findUserByUserId(editDTO.getUser_id())
                .orElseThrow(() -> new UserException("사용자를 찾을 수 없습니다."));

        UserRole userRole = userRepository.getUserRole(user.getCurrentNHResident(), user.getUserId())
                .orElseThrow(() -> new UserException("역할을 찾을 수 없습니다."));

        if (userRole == UserRole.PROTECTOR)
            throw new UserAuthException("일정을 수정할 권한이 없습니다.");

        schedule.editSchedule(user, editDTO.getDate(), editDTO.getTexts());
    }

    public void delete(ScheduleDeleteDTO deleteDTO) throws Exception {

        Schedule schedule = scheduleRepository.findById(deleteDTO.getSchedule_id())
                .orElseThrow(() -> new ScheduleException("해당 일정을 찾을 수 없습니다."));

        User user = userRepository.findUserByUserId(deleteDTO.getUser_id())
                .orElseThrow(() -> new UserException("사용자를 찾을 수 없습니다."));

        UserRole userRole = userRepository.getUserRole(user.getCurrentNHResident(), user.getUserId())
                .orElseThrow(() -> new UserException("역할을 찾을 수 없습니다."));

        if (userRole == UserRole.PROTECTOR)
            throw new UserAuthException("권한이 없는 사용자입니다.");

        scheduleRepository.delete(schedule);
    }
//내림차순으로 받아오는게 좋은듯
    public List<ScheduleListDTO> scheduleList(Long facility_id) throws Exception {
        Facility facility = facilityRepository.findById(facility_id)
                .orElseThrow(() -> new FacilityException("해당 시설을 찾을 수 없습니다."));

        List<Schedule> schedules = scheduleRepository.findAllByFacility(facility)
                .orElse(new ArrayList<>());

        List<ScheduleListDTO> listDTOS = new ArrayList<>();

        for (Schedule schedule : schedules) {
            listDTOS.add(ScheduleListDTO.builder()
                    .schedule_id(schedule.getId())
                    .date(schedule.getDates())
                    .texts(schedule.getTexts()).build());
        }

        return listDTOS;
    }

    public List<ScheduleListDTO> monthlyList(Long facility_id, String yearMonth) {

      List<Schedule> schedules = scheduleRepository.findAllByMonth(facility_id, yearMonth)
              .orElse(new ArrayList<>());

      List<ScheduleListDTO> listDTOS = new ArrayList<>();

      for (Schedule schedule : schedules) {
        listDTOS.add(ScheduleListDTO.builder()
                .schedule_id(schedule.getId())
                .date(schedule.getDates())
                .texts(schedule.getTexts()).build());
      }

      return listDTOS;
    }
}
