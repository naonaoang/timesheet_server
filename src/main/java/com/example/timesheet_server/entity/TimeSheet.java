package com.example.timesheet_server.entity;

import lombok.*;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Document(collection = "timeSheet")
public class TimeSheet {
    @Id
    private String id;
    private int userId;
    private Set<String> weeks;
    private int floatingDaysLeft;
    private int vacationDaysLeft;

    @PersistenceConstructor
    public TimeSheet(String id, int userId, Set<String> weeks, int floatingDaysLeft, int vacationDaysLeft){
        this.id = id;
        this.userId = userId;
        this.weeks = weeks;
        this.floatingDaysLeft = floatingDaysLeft;
        this.vacationDaysLeft = vacationDaysLeft;
    }
}
