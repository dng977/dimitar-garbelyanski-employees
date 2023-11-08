package com.dimtar.garbelyanski.employees.model;

import lombok.AllArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
public class Employee {
    private final UUID employeeId;
    private String name;
}
