package com.example.timesheet_server.service;

import com.example.timesheet_server.dao.TemplateRepo;
import com.example.timesheet_server.dao.TimeSheetRepo;
import com.example.timesheet_server.dao.WeekRepo;
import com.example.timesheet_server.dao.WeekdayRepo;
import com.example.timesheet_server.domain.TimeSheetDomain;
import com.example.timesheet_server.domain.WeekDomain;
import com.example.timesheet_server.domain.WeekdayDomain;
import com.example.timesheet_server.domain.common.GeneralResponse;
import com.example.timesheet_server.entity.Template;
import com.example.timesheet_server.entity.TimeSheet;
import com.example.timesheet_server.entity.Week;
import com.example.timesheet_server.entity.Weekday;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TimeSheetService {
    @Autowired
    private TimeSheetRepo timeSheetRepo;
    @Autowired
    private WeekdayRepo weekdayRepo;
    @Autowired
    private WeekRepo weekRepo;
    @Autowired
    private TemplateRepo templateRepo;

    public TimeSheetDomain getTimeSheetByUserId(Integer userId){
        Optional<TimeSheet> timeSheetOptional = timeSheetRepo.findByUserId(userId);
        return timeSheetToDomain(timeSheetOptional.get());
    }

    public WeekDomain getTimeSheetByUserIdAndStartDate(String startDate, int userId){
        Optional<Week> weekOptional = weekRepo.findWeekByStartDateAndUserId(startDate, userId);
        return weekToDomain(weekOptional.get());
    }
    public Set<String> getWeekdayIds(String startDate, int userId){
        Optional<Week> weekOptional = weekRepo.findWeekByStartDateAndUserId(startDate, userId);
        return weekOptional.get().getWeekdays();
    }

    public Weekday getWeekdayById(String id){
        return weekdayRepo.findById(id).get();
    }

    public void updateWeekday(Weekday weekday){
        weekdayRepo.save(weekday);
    }

    public void updateWeek(Week week){
        weekRepo.save(week);
    }

    public Week getWeekByUserIdAndStartDate(String startDate, int userId){
        return weekRepo.findWeekByStartDateAndUserId(startDate, userId).get();
    }

    public void createTimeSheet(int userId, String startDate){
        Set<String> newWeekdayIds = new LinkedHashSet<>();
        Optional<Template> optionalTemplate = getTemplateByUserId(userId);
        Week newWeek = new Week();
        String[] days = {"Mon", "Tue", "Wed", "Thur", "Fri"};
        if(!optionalTemplate.isPresent()){
            for(int i = 0; i < 5; i++){
                Weekday newDay = new Weekday();
                newDay.setDay(days[i]);
                newDay.setDate((LocalDate.parse(startDate).plusDays(i)).toString());
                newDay.setStartTime(null);
                newDay.setEndTime(null);
                newDay.setFloatingDay(false);
                newDay.setHoliday(false);
                newDay.setVacation(false);
                weekdayRepo.insert(newDay);
                newWeekdayIds.add(newDay.getId());
            }
            newWeek.setTotalCompensatedHour(0);
            newWeek.setTotalBillingHour(0);
            newWeek.setFloatingDaysUsed(0);
            newWeek.setVacationDaysUsed(0);
            newWeek.setStartDate(startDate);
            newWeek.setUserId(userId);
            newWeek.setWeekdays(newWeekdayIds);
            weekRepo.insert(newWeek);
        }else{
            Template template = optionalTemplate.get();
            for(int i = 0; i < 5; i++){
                Weekday newDay = new Weekday();
                WeekdayDomain weekdayDomain = template.getWeekdayTemplate().get(i);
                newDay.setDay(weekdayDomain.getDay());
                newDay.setDate((LocalDate.parse(startDate).plusDays(i)).toString());
                newDay.setStartTime(weekdayDomain.getStartTime());
                newDay.setEndTime(weekdayDomain.getEndTime());
                newDay.setFloatingDay(weekdayDomain.getIsFloatingDay());
                newDay.setHoliday(weekdayDomain.getIsHoliday());
                newDay.setVacation(weekdayDomain.getIsVacation());
                weekdayRepo.insert(newDay);
                newWeekdayIds.add(newDay.getId());
            }
            newWeek.setTotalCompensatedHour(template.getTotalCompensatedHour());
            newWeek.setTotalBillingHour(template.getTotalBillingHour());
            newWeek.setFloatingDaysUsed(template.getFloatingDaysUsed());
            newWeek.setVacationDaysUsed(template.getVacationDaysUsed());
            newWeek.setStartDate(startDate);
            newWeek.setUserId(userId);
            newWeek.setWeekdays(newWeekdayIds);
            weekRepo.insert(newWeek);
        }
        TimeSheet timeSheet = timeSheetRepo.findByUserId(userId).get();
        Set<String> weekIds = timeSheet.getWeeks();
        weekIds.add(newWeek.getId());
        timeSheet.setWeeks(weekIds);
        timeSheetRepo.save(timeSheet);
    }

    public void createTemplate(Template template){
        templateRepo.save(template);
    }

    public Optional<Template> getTemplateByUserId(int userId){
        return templateRepo.findByUserId(userId);
    }

    TimeSheetDomain timeSheetToDomain(TimeSheet timeSheet){
        Set<String> weekIds = timeSheet.getWeeks();
        Set<WeekDomain> weeks = weekIds.stream()
                .map(id -> weekRepo.findById(id))
                .filter(o -> o.isPresent())
                .map(o -> {
                    Week w = o.get();
                    return weekToDomain(w);
                }).collect(Collectors.toCollection(LinkedHashSet::new));
        return TimeSheetDomain.builder()
                .userId(timeSheet.getUserId())
                .weeks(weeks)
                .floatingDaysLeft(timeSheet.getFloatingDaysLeft())
                .vacationDaysLeft(timeSheet.getVacationDaysLeft())
                .build();
    }

    WeekDomain weekToDomain(Week week) {
        Set<String> weekdayIds = week.getWeekdays();

        Set<WeekdayDomain> weekdays = weekdayIds.stream()
                .map(id -> weekdayRepo.findById(id))
                .filter(o -> o.isPresent())
                .map(o ->{
                    Weekday w = o.get();
                    return WeekdayDomain.builder()
                            .date(w.getDate())
                            .day(w.getDay())
                            .startTime(w.getStartTime())
                            .endTime(w.getEndTime())
                            .isHoliday(w.getHoliday())
                            .isFloatingDay(w.getFloatingDay())
                            .isVacation(w.getVacation())
                            .build();
                }).collect(Collectors.toCollection(LinkedHashSet::new));
        return WeekDomain.builder()
                .startDate(week.getStartDate())
                .weekdays(weekdays)
                .floatingDaysUsed(week.getFloatingDaysUsed())
                .totalBillingHour(week.getTotalBillingHour())
                .totalCompensatedHour(week.getTotalCompensatedHour())
                .vacationDaysUsed(week.getVacationDaysUsed())
                .build();
    }
}
