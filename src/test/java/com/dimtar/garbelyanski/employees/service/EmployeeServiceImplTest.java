package com.dimtar.garbelyanski.employees.service;

import com.dimtar.garbelyanski.employees.config.WebConfig;
import com.dimtar.garbelyanski.employees.controller.EmployeesController;
import com.dimtar.garbelyanski.employees.dto.WorkTogetherRecord;
import com.dimtar.garbelyanski.employees.exception.BadCsvFormatException;
import com.dimtar.garbelyanski.employees.model.EmployeeWorkRecord;
import org.hibernate.jdbc.Work;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


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
                LocalDate.of(2020, Month.OCTOBER,1)));
        employeeWorkRecords.add(new EmployeeWorkRecord(
                2L,
                1L,
                LocalDate.of(2020, Month.APRIL,1),
                LocalDate.of(2020, Month.OCTOBER,1)));
        employeeWorkRecords.add(new EmployeeWorkRecord(
                3L,
                1L,
                LocalDate.of(2019, Month.APRIL,1),
                LocalDate.of(2021, Month.OCTOBER,1)));
        employeeWorkRecords.add(new EmployeeWorkRecord(
                2L,
                2L,
                LocalDate.of(2020, Month.APRIL,1),
                LocalDate.of(2020, Month.APRIL,15)));
        employeeWorkRecords.add(new EmployeeWorkRecord(
                3L,
                2L,
                LocalDate.of(2020, Month.APRIL,11),
                LocalDate.of(2020, Month.APRIL,30)));

        List<WorkTogetherRecord> workTogetherRecords = employeeService.getEmployeesWorkedTogether(employeeWorkRecords);

        for (WorkTogetherRecord record: workTogetherRecords){
            System.out.println(record);
        }
    }

    @Test
    void getEmployeesWorkedTogetherCSV() throws BadCsvFormatException, IOException {
        MockMultipartFile file
                = new MockMultipartFile(
                "file",
                "hello.csv",
                MediaType.TEXT_PLAIN_VALUE,
                """
                        143, 12, 2013-11-01, 2014-01-05
                        218, 10, 16-05-2012, NULL
                        143, 10, 01/01/2009, 2015-04-27
                        """.getBytes()
        );


        List<WorkTogetherRecord> workTogetherRecords = employeeService.getEmployeesWorkedTogether(file);

        for (WorkTogetherRecord record: workTogetherRecords){
            System.out.println(record);
        }
    }


}