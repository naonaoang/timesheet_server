package com.example.timesheet_server.dao;

import com.example.timesheet_server.entity.Template;
import com.example.timesheet_server.entity.TimeSheet;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface TemplateRepo extends MongoRepository<Template, String> {
    Optional<Template> findByUserId(Integer userId);
}
