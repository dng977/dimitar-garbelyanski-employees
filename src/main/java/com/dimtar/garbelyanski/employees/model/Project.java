package com.dimtar.garbelyanski.employees.model;

import lombok.AllArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
public class Project {
    private final UUID projectId;
    private String projectName;
    private EmployeeWorkRecord employeeWorkRecord;
}
