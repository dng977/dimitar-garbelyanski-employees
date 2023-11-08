package com.dimtar.garbelyanski.employees.service;

import com.dimtar.garbelyanski.employees.dto.WorkTogetherRecord;
import com.dimtar.garbelyanski.employees.exception.BadCsvFormatException;
import com.dimtar.garbelyanski.employees.model.EmployeeWorkRecord;
import com.dimtar.garbelyanski.employees.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Slf4j
@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Override
    public List<WorkTogetherRecord> getEmployeesWorkedTogether(MultipartFile csvFile) throws IOException, BadCsvFormatException {
        List<EmployeeWorkRecord> employeeWorkRecords = parseProjectHistoryDataFromCSV(csvFile);
        return getEmployeesWorkedTogether(employeeWorkRecords);
    }


    public List<WorkTogetherRecord> getEmployeesWorkedTogether(List<EmployeeWorkRecord> employeeWorkRecords){
        List<WorkTogetherRecord> workTogetherRecords = new ArrayList<>();

        Map<Long, List<EmployeeWorkRecord>> projectEmployeesMap = new HashMap<>();

        for(EmployeeWorkRecord employeeWorkRecord : employeeWorkRecords){
            Long projectId = employeeWorkRecord.getProjectId();

            if(projectEmployeesMap.containsKey(projectId)){
                List<EmployeeWorkRecord> employeesOnThisProject = projectEmployeesMap.get(projectId);

                for(EmployeeWorkRecord previousEmployeeWorkRecord : employeesOnThisProject){
                    if(!employeeWorkRecord.getEmployeeId().equals(previousEmployeeWorkRecord.getEmployeeId())){
                        LocalDate startDate1 = employeeWorkRecord.getDateFrom();
                        LocalDate endDate1 = employeeWorkRecord.getDateTo();
                        LocalDate startDate2 = previousEmployeeWorkRecord.getDateFrom();
                        LocalDate endDate2 = previousEmployeeWorkRecord.getDateTo();

                        //If there is overlap
                        if(!startDate1.isAfter(endDate2)
                                && !startDate2.isAfter(endDate1)){

                            LocalDate commonStartDate = startDate1.isBefore(startDate2) ? startDate2 : startDate1;
                            LocalDate commonEndDate = endDate1.isBefore(endDate2) ? endDate1 : endDate2;
                            int daysWorkedTogether = (int) ChronoUnit.DAYS.between(commonStartDate,commonEndDate) + 1 ;// + 1 to include endDate

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

            }else{
                projectEmployeesMap.put(projectId, new ArrayList<>(List.of(employeeWorkRecord)));
            }
        }

        return workTogetherRecords.stream().sorted(Comparator.comparing(WorkTogetherRecord::getDaysWorked,Comparator.reverseOrder())).toList();
    }

    public List<EmployeeWorkRecord> parseProjectHistoryDataFromCSV(MultipartFile csvFile) throws IOException, BadCsvFormatException {
        List<List<String>> records = FileUtil.scanProjectsData(csvFile.getInputStream());

        List<EmployeeWorkRecord> projectHistories = new ArrayList<>();
        for(List<String> line: records){
            EmployeeWorkRecord.EmployeeWorkRecordBuilder projectHistoryBuilder = EmployeeWorkRecord.builder()
                    .employeeId(Long.parseLong(line.get(0)))
                    .projectId(Long.parseLong(line.get(1)));
            try{
                DateTimeFormatterBuilder dateTimeFormatterBuilder = new DateTimeFormatterBuilder()
                        .append(DateTimeFormatter.ofPattern("[MM/dd/yyyy]" + "[dd-MM-yyyy]" + "[yyyy-MM-dd]" + "[dd/MM/yyyy]" + "[yyyy/MM/dd]" ));
                DateTimeFormatter dateTimeFormatter = dateTimeFormatterBuilder.toFormatter();
                projectHistoryBuilder.dateFrom(LocalDate.parse(line.get(2),dateTimeFormatter));

                if(line.get(3).equalsIgnoreCase("null")){
                    projectHistoryBuilder.dateTo(LocalDate.now());
                }else{
                    projectHistoryBuilder.dateTo(LocalDate.parse(line.get(3))).build();
                }
            }catch (DateTimeParseException e){
                log.error("Bad date format", e);
                throw new BadCsvFormatException("Bad date format: " + line.get(2));
            }
            projectHistories.add(projectHistoryBuilder.build());
        }

        return projectHistories;
    }
}



