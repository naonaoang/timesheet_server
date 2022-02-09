package com.example.timesheet_server.domain;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.Set;

@Data
@Builder
@ToString
public class TimeSheetDomain {
    private int userId;
    private Set<WeekDomain> weeks;
    private int floatingDaysLeft;
    private int vacationDaysLeft;
}
