package com.dimtar.garbelyanski.employees.dto;

import com.dimtar.garbelyanski.employees.model.EmployeeWorkRecord;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.jdbc.Work;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;


@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Data
public class WorkTogetherRecord {
    private final Long employee1Id;
    private final long employee2Id;
    private final long projectId;
    private final int daysWorked;

    public static List<WorkTogetherRecord> calculateWorkTogetherRecords(List<EmployeeWorkRecord> employeeWorkRecords){
        List<WorkTogetherRecord> workTogetherRecords = new ArrayList<>();

        Map<Long, List<EmployeeWorkRecord>> projectEmployeesMap = new HashMap<>();

        for (EmployeeWorkRecord employeeWorkRecord : employeeWorkRecords) {
            Long projectId = employeeWorkRecord.getProjectId();

            if (projectEmployeesMap.containsKey(projectId)) {
                List<EmployeeWorkRecord> employeesOnThisProject = projectEmployeesMap.get(projectId);

                for (EmployeeWorkRecord previousEmployeeWorkRecord : employeesOnThisProject) {
                    if (!employeeWorkRecord.getEmployeeId().equals(previousEmployeeWorkRecord.getEmployeeId())) {
                        LocalDate startDate1 = employeeWorkRecord.getDateFrom();
                        LocalDate endDate1 = employeeWorkRecord.getDateTo();
                        LocalDate startDate2 = previousEmployeeWorkRecord.getDateFrom();
                        LocalDate endDate2 = previousEmployeeWorkRecord.getDateTo();

                        //If there is overlap
                        if (!startDate1.isAfter(endDate2)
                                && !startDate2.isAfter(endDate1)) {

                            LocalDate commonStartDate = startDate1.isBefore(startDate2) ? startDate2 : startDate1;
                            LocalDate commonEndDate = endDate1.isBefore(endDate2) ? endDate1 : endDate2;
                            int daysWorkedTogether = (int) ChronoUnit.DAYS.between(commonStartDate, commonEndDate) + 1;// + 1 to include endDate

                            WorkTogetherRecord workTogetherRecord = new WorkTogetherRecord(
                                    employeeWorkRecord.getEmployeeId(),
                                    previousEmployeeWorkRecord.getEmployeeId(),
                                    projectId,
                                    daysWorkedTogether
                            );

                            workTogetherRecords.add(workTogetherRecord);
                        }
                    }

                }

                //Add this employee to the map
                projectEmployeesMap.get(projectId).add(employeeWorkRecord);

            } else {
                projectEmployeesMap.put(projectId, new ArrayList<>(List.of(employeeWorkRecord)));
            }
        }

        return workTogetherRecords.stream().sorted(Comparator.comparing(WorkTogetherRecord::getDaysWorked, Comparator.reverseOrder())).toList();

    }

}
