package com.dimtar.garbelyanski.employees.model;

import com.dimtar.garbelyanski.employees.exception.BadCsvFormatException;
import com.dimtar.garbelyanski.employees.exception.BadFormatException;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Builder
@Getter
@Slf4j
public class EmployeeWorkRecord {
    private final Long employeeId;
    private final Long projectId;
    private final LocalDate dateFrom;
    private final LocalDate dateTo;

    public static List<EmployeeWorkRecord> buildFromDataSource(DataSource dataSource) throws BadFormatException, IOException {
        List<List<String>> records = dataSource.readData();

        List<EmployeeWorkRecord> projectHistories = new ArrayList<>();
        for (List<String> line : records) {
            try {
                EmployeeWorkRecord.EmployeeWorkRecordBuilder projectHistoryBuilder = EmployeeWorkRecord.builder()
                        .employeeId(Long.parseLong(line.get(0)))
                        .projectId(Long.parseLong(line.get(1)));

                DateTimeFormatterBuilder dateTimeFormatterBuilder = new DateTimeFormatterBuilder()
                        .append(DateTimeFormatter.ofPattern("[dd-MM-yyyy]" + "[yyyy-MM-dd]" + "[dd/MM/yyyy]" + "[yyyy/MM/dd]" + "[dd/MM/yy]"
                                + "[dd-MM-yy]"));
                DateTimeFormatter dateTimeFormatter = dateTimeFormatterBuilder.toFormatter();
                projectHistoryBuilder.dateFrom(LocalDate.parse(line.get(2), dateTimeFormatter));

                if (line.get(3).equalsIgnoreCase("null")) {
                    projectHistoryBuilder.dateTo(LocalDate.now());
                } else {
                    projectHistoryBuilder.dateTo(LocalDate.parse(line.get(3), dateTimeFormatter)).build();
                }


                projectHistories.add(projectHistoryBuilder.build());

            } catch (DateTimeParseException e) {
                log.info("Bad date format");
                throw new BadCsvFormatException("Bad date format: " + line.get(2) + "!");
            }catch (Exception e){
                log.info("Bad format", e);
                throw new BadCsvFormatException("Invalid format!");

            }
        }

        return projectHistories;
    }

}
