package com.example.timesheet_server.domain.common;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class ServiceStatus implements Serializable {
    private String statusCode;
    private boolean success;
    private String errorMessage;
}
