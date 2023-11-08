package com.dimtar.garbelyanski.employees.model;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@RequiredArgsConstructor
@Builder
@Getter
public class EmployeeWorkRecord {
    private final Long employeeId;
    private final Long projectId;
    private final LocalDate dateFrom;
    private final LocalDate dateTo;


}
