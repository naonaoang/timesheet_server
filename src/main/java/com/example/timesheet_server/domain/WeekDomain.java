package com.example.timesheet_server.domain;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.Set;

@Data
@Builder
@ToString
public class WeekDomain {
    private String startDate;
    private Set<WeekdayDomain> weekdays;
    private int totalBillingHour;
    private int totalCompensatedHour;
    private int floatingDaysUsed;
    private int vacationDaysUsed;
    private String submissionStatus;
    private String approvalStatus;
}
