package com.example.timesheet_server.entity;

import com.example.timesheet_server.domain.WeekdayDomain;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Document(collection = "template")
public class Template {
    @Id
    private String id;
    private int userId;
    private List<WeekdayDomain> weekdayTemplate;
    private int totalBillingHour;
    private int totalCompensatedHour;
    private int floatingDaysUsed;
    private int vacationDaysUsed;

    public void setWeekdays(List<LinkedHashMap> weekdayDomain){
        this.weekdayTemplate = new ArrayList<WeekdayDomain>();
        for(int i = 0; i<5; i++){
            WeekdayDomain weekday = WeekdayDomain.builder().build();
            weekday.setDay((String)weekdayDomain.get(i).get("day"));
            weekday.setDate((String)weekdayDomain.get(i).get("date"));
            weekday.setStartTime((Integer)weekdayDomain.get(i).get("startTime"));
            weekday.setEndTime((Integer)weekdayDomain.get(i).get("endTime"));
            weekday.setIsFloatingDay((Boolean)weekdayDomain.get(i).get("isFloatingDay"));
            weekday.setIsHoliday((Boolean)weekdayDomain.get(i).get("isHoliday"));
            weekday.setIsVacation((Boolean)weekdayDomain.get(i).get("isVacation"));
            this.weekdayTemplate.add(weekday);
        }
    }
}
