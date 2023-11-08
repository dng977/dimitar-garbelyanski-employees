package com.dimtar.garbelyanski.employees.service;

import com.dimtar.garbelyanski.employees.dto.WorkTogetherRecord;
import com.dimtar.garbelyanski.employees.exception.BadCsvFormatException;
import com.dimtar.garbelyanski.employees.model.EmployeeWorkRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
class EmployeeServiceImplTest {

    @Autowired
    EmployeeServiceImpl employeeService;


    @Test
    void getEmployeesWorkedTogether() {
        List<EmployeeWorkRecord> employeeWorkRecords = new ArrayList<>();
        employeeWorkRecords.add(new EmployeeWorkRecord(
                1L,
                1L,
                LocalDate.of(2020, Month.APRIL,1),
                LocalDate.of(2020, Month.APRIL,15)));
        employeeWorkRecords.add(new EmployeeWorkRecord(
                2L,
                1L,
                LocalDate.of(2020, Month.APRIL,10),
                LocalDate.of(2020, Month.APRIL,15)));
        employeeWorkRecords.add(new EmployeeWorkRecord(
                3L,
                1L,
                LocalDate.of(2019, Month.APRIL,1),
                LocalDate.of(2021, Month.OCTOBER,1)));

        List<WorkTogetherRecord> workTogetherRecords = employeeService.getEmployeesWorkedTogether(employeeWorkRecords);

        assertEquals(3, workTogetherRecords.size());
        assertEquals(15, workTogetherRecords.get(0).getDaysWorked());
        assertEquals(6, workTogetherRecords.get(1).getDaysWorked());
    }

    @Test
    void getEmployeesWorkedTogetherCSV() throws BadCsvFormatException, IOException {
        MockMultipartFile file
                = new MockMultipartFile(
                "file",
                "hello.csv",
                MediaType.TEXT_PLAIN_VALUE,
                """
                        143, 12, 2013-11-01, 15-11-2013
                        218, 10, 16/05/12, NULL
                        143, 10, 16/05/2012, 25-05-12
                        """.getBytes()
        );


        List<WorkTogetherRecord> workTogetherRecords = employeeService.getEmployeesWorkedTogether(file);

        assertEquals(1,workTogetherRecords.size());
        assertEquals(10,workTogetherRecords.get(0).getDaysWorked());
    }


    @Test
    void getEmployeesWorkedTogetherCSVBadDateFormat() {
        MockMultipartFile file
                = new MockMultipartFile(
                "file",
                "hello.csv",
                MediaType.TEXT_PLAIN_VALUE,
                """
                        143, 12, 2013-11-01, 15-11-20135
                        218, 10, 55/05/12, NULL
                        143, 10, 16/05/2012, 25-05-12
                        """.getBytes()
        );


        assertThrows(BadCsvFormatException.class, () -> employeeService.getEmployeesWorkedTogether(file));

    }


}