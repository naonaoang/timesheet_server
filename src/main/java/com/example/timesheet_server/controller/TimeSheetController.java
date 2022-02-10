package com.example.timesheet_server.controller;

import com.example.timesheet_server.dao.TemplateRepo;
import com.example.timesheet_server.domain.*;
import com.example.timesheet_server.domain.common.GeneralResponse;
import com.example.timesheet_server.domain.common.ServiceStatus;
import com.example.timesheet_server.entity.Template;
import com.example.timesheet_server.entity.Week;
import com.example.timesheet_server.entity.Weekday;
import com.example.timesheet_server.service.TimeSheetService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/timeSheet")
public class TimeSheetController {
    @Autowired
    private TimeSheetService timeSheetService;

    @GetMapping("/{userId}")
    public TimeSheetResponse getTimeSheetByUserId(@PathVariable Integer userId){
        TimeSheetDomain timeSheetDomain = timeSheetService.getTimeSheetByUserId(userId);
        return new TimeSheetResponse(
                new ServiceStatus("SUCCEED", true, ""), timeSheetDomain
        );
    }

    @GetMapping("/{userId}/{startDate}")
    public SingleWeekResponse getTimeSheetByUserIdAndStartDate(@PathVariable int userId, @PathVariable String startDate){
        WeekDomain weekDomain = timeSheetService.getTimeSheetByUserIdAndStartDate(startDate, userId);
        return new SingleWeekResponse(
                new ServiceStatus("SUCCEED", true, ""), weekDomain
        );
    }

    @PutMapping("/{userId}/{startDate}")
    public void updateTimeSheet(@PathVariable int userId, @RequestBody Map<String, Object> payload){
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

    @PostMapping("/add")
    public void addNewTimeSheet(){
        int userId = 1;
        String startDate = "2022-02-07";
        timeSheetService.createTimeSheet(userId, startDate);
    }

    @PostMapping("/{userId}/save")
    public void saveTemplate(@PathVariable int userId, @RequestBody Map<String, Object> payload){
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
