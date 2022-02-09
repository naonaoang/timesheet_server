package com.example.timesheet_server.entity;

import com.example.timesheet_server.domain.WeekdayDomain;
import lombok.*;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.util.LinkedHashMap;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Document(collection = "weekday")
public class Weekday {
    @Id
    private String id;
    private String day;//Mon, Tue, Wed, Thur, Fri
    private String date;
    private Integer startTime;
    private Integer endTime;
    private Boolean floatingDay;
    private Boolean holiday;
    private Boolean vacation;

    @PersistenceConstructor
    public Weekday(String id, String day, String date, Integer startTime, Integer endTime, Boolean floatingDay, Boolean holiday, Boolean vacation){
        this.id = id;
        this.date = date;
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
        this.floatingDay = floatingDay;
        this.holiday = holiday;
        this.vacation = vacation;
    }

    public void update(LinkedHashMap weekdayDomain){
        this.day = (String)weekdayDomain.get("day");
        this.date = (String)weekdayDomain.get("date");
        this.startTime = (Integer)weekdayDomain.get("startTime");
        this.endTime = (Integer)weekdayDomain.get("endTime");
        this.floatingDay =(Boolean)weekdayDomain.get("isFloatingDay");
        this.holiday = (Boolean)weekdayDomain.get("isHoliday");
        this.vacation = (Boolean)weekdayDomain.get("isVacation");
    }
}
