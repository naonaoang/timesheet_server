package com.example.timesheet_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class TimesheetServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TimesheetServerApplication.class, args);
    }

}
