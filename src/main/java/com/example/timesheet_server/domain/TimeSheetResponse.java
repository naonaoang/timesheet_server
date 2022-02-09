package com.example.timesheet_server.domain;

import com.example.timesheet_server.domain.common.ServiceStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TimeSheetResponse implements Serializable {
    private ServiceStatus serviceStatus;
    @JsonProperty("TimeSheet")
    private TimeSheetDomain timeSheetDomain;
}
