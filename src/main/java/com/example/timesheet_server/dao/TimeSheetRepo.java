package com.example.timesheet_server.dao;

import com.example.timesheet_server.entity.TimeSheet;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface TimeSheetRepo extends MongoRepository<TimeSheet, String> {
    Optional<TimeSheet> findByUserId(Integer userId);
}
