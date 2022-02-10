package com.example.timesheet_server.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@ToString
public class WeekdayDomain {
    private String day;//Mon, Tue, Wed, Thur, Fri
    private String date;
    private Integer startTime;
    private Integer endTime;
    private Boolean isFloatingDay;
    private Boolean isHoliday;
    private Boolean isVacation;

}
