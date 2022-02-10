package com.example.timesheet_server.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Document(collection = "week")
public class Week {
    @Id
    private String id;
    private int userId;
    private String startDate;
    private int totalBillingHour;
    private int totalCompensatedHour;
    private int floatingDaysUsed;
    private int vacationDaysUsed;
    private Set<String> weekdays;
    private String submissionStatus;
    private String approvalStatus;

    @PersistenceConstructor
    public Week(String id, int userId, String startDate, int totalBillingHour, int totalCompensatedHour, int floatingDaysUsed, int vacationDaysUsed, Set<String> weekdays, String submissionStatus, String approvalStatus){
        this.id = id;
        this.userId = userId;
        this.startDate = startDate;
        this.totalBillingHour = totalBillingHour;
        this.totalCompensatedHour = totalCompensatedHour;
        this.floatingDaysUsed = floatingDaysUsed;
        this.vacationDaysUsed = vacationDaysUsed;
        this.weekdays = weekdays;
        this.submissionStatus = submissionStatus;
        this.approvalStatus = approvalStatus;
    }
}
