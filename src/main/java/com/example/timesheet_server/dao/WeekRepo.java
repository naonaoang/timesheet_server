package com.example.timesheet_server.dao;

import com.example.timesheet_server.entity.Week;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface WeekRepo extends MongoRepository<Week, String> {
//    @Query("{'startDate' : ?0, 'userId' : ?1}")
    Optional<Week> findWeekByStartDateAndUserId(String startDate, int userId);
}
