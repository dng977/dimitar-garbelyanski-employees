package com.dimtar.garbelyanski.employees.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;


@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Data
public class WorkTogetherRecord {
    private final Long employee1Id;
    private final long employee2Id;
    private final long projectId;
    private final int daysWorked;

}
