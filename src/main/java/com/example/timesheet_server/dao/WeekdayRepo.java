package com.example.timesheet_server.dao;

import com.example.timesheet_server.entity.Weekday;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface WeekdayRepo extends MongoRepository<Weekday, String> {
}
