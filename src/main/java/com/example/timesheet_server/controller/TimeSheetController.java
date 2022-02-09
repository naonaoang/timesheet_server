package com.example.timesheet_server.controller;

import com.example.timesheet_server.domain.*;
import com.example.timesheet_server.domain.common.GeneralResponse;
import com.example.timesheet_server.domain.common.ServiceStatus;
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
    public void updateTimeSheet(@PathVariable int userId, @PathVariable String startDate, @RequestBody Map<String, Object> payload){
        List<LinkedHashMap> weekdayDomain = (List<LinkedHashMap>) payload.get("weekdays");
        List<String> weekdayIds = new ArrayList<>();
        weekdayIds.addAll(timeSheetService.getWeekdayIds(startDate, userId));
        Weekday test = new Weekday();
        for(int i = 0; i < weekdayIds.size(); i++){
            Weekday weekday = timeSheetService.getWeekdayById(weekdayIds.get(i));
            weekday.update(weekdayDomain.get(i));
            timeSheetService.updateWeekday(weekday);
        }
        Week week = timeSheetService.getWeekByUserIdAndStartDate(startDate, userId);
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

    @ExceptionHandler(DataAccessException.class)
    public GeneralResponse repoException() {

        return new GeneralResponse(new ServiceStatus("FAIL", false, "Database exception"));
    }
}
