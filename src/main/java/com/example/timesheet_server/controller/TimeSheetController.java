package com.example.timesheet_server.controller;

import com.example.timesheet_server.constant.JwtConstant;
import com.example.timesheet_server.dao.TemplateRepo;
import com.example.timesheet_server.domain.*;
import com.example.timesheet_server.domain.common.GeneralResponse;
import com.example.timesheet_server.domain.common.ServiceStatus;
import com.example.timesheet_server.entity.Template;
import com.example.timesheet_server.entity.Week;
import com.example.timesheet_server.entity.Weekday;
import com.example.timesheet_server.security.util.CookieUtil;
import com.example.timesheet_server.security.util.JwtUtil;
import com.example.timesheet_server.service.TimeSheetService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/timesheet-service")
public class TimeSheetController {
    @Autowired
    private TimeSheetService timeSheetService;

    @GetMapping("/summary")
    public TimeSheetResponse getTimeSheetByUserId(HttpServletRequest request){
        int userId = JwtUtil.getSubjectFromJwt(CookieUtil.getValue(request, JwtConstant.JWT_COOKIE_NAME));
        TimeSheetDomain timeSheetDomain = timeSheetService.getTimeSheetByUserId(userId);
        return new TimeSheetResponse(
                new ServiceStatus("SUCCEED", true, ""), timeSheetDomain
        );
    }

    @GetMapping("/weekly/{startDate}")
    public SingleWeekResponse getTimeSheetByUserIdAndStartDate(@PathVariable String startDate, HttpServletRequest request){
        int userId = JwtUtil.getSubjectFromJwt(CookieUtil.getValue(request, JwtConstant.JWT_COOKIE_NAME));
        WeekDomain weekDomain = timeSheetService.getTimeSheetByUserIdAndStartDate(startDate, userId);
        return new SingleWeekResponse(
                new ServiceStatus("SUCCEED", true, ""), weekDomain
        );
    }

    @PutMapping("/update")
    public void updateTimeSheet(@RequestBody Map<String, Object> payload, HttpServletRequest request){
        int userId = JwtUtil.getSubjectFromJwt(CookieUtil.getValue(request, JwtConstant.JWT_COOKIE_NAME));
        List<LinkedHashMap> weekdayDomain = (List<LinkedHashMap>) payload.get("weekdays");
        List<String> weekdayIds = new ArrayList<>();
        weekdayIds.addAll(timeSheetService.getWeekdayIds((String)payload.get("startDate"), userId));
        for(int i = 0; i < weekdayIds.size(); i++){
            Weekday weekday = timeSheetService.getWeekdayById(weekdayIds.get(i));
            weekday.update(weekdayDomain.get(i));
            timeSheetService.updateWeekday(weekday);
        }
        Week week = timeSheetService.getWeekByUserIdAndStartDate((String)payload.get("startDate"), userId);
        week.setFloatingDaysUsed((int)payload.get("floatingDaysUsed"));
        week.setVacationDaysUsed((int)payload.get("vacationDaysUsed"));
        week.setTotalBillingHour((int)payload.get("totalBillingHour"));
        week.setTotalCompensatedHour((int)payload.get("totalCompensatedHour"));
        timeSheetService.updateWeek(week);
    }

    @Scheduled(cron = "0 0 0 * * MON")
    @PostMapping("/add")
    public void addNewTimeSheet(HttpServletRequest request){
        int userId = JwtUtil.getSubjectFromJwt(CookieUtil.getValue(request, JwtConstant.JWT_COOKIE_NAME));
        //String startDate = LocalDate.now().plusDays(7).toString();
        String startDate = "2022-02-07";
        timeSheetService.createTimeSheet(userId, startDate);
    }

    @PostMapping("/save")
    public void saveTemplate(@RequestBody Map<String, Object> payload, HttpServletRequest request){
        int userId = JwtUtil.getSubjectFromJwt(CookieUtil.getValue(request, JwtConstant.JWT_COOKIE_NAME));
        List<LinkedHashMap> weekdayDomain = (List<LinkedHashMap>) payload.get("weekdays");
        Optional<Template> optionalTemplate = timeSheetService.getTemplateByUserId(userId);
        Template template = new Template();
        if(optionalTemplate.isPresent()){
            template.setId(optionalTemplate.get().getId());
        }
        template.setUserId(userId);
        template.setFloatingDaysUsed((Integer) payload.get("floatingDaysUsed"));
        template.setVacationDaysUsed((Integer) payload.get("vacationDaysUsed"));
        template.setTotalCompensatedHour((Integer) payload.get("totalCompensatedHour"));
        template.setTotalBillingHour((Integer) payload.get("totalBillingHour"));
        template.setWeekdays(weekdayDomain);
        timeSheetService.createTemplate(template);
    }

    @ExceptionHandler(DataAccessException.class)
    public GeneralResponse repoException() {

        return new GeneralResponse(new ServiceStatus("FAIL", false, "Database exception"));
    }
}
