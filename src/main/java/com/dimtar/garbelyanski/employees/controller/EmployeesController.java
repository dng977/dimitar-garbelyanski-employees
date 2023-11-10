package com.dimtar.garbelyanski.employees.controller;

import com.dimtar.garbelyanski.employees.dto.WorkTogetherRecord;
import com.dimtar.garbelyanski.employees.exception.BadCsvFormatException;
import com.dimtar.garbelyanski.employees.exception.BadFormatException;
import com.dimtar.garbelyanski.employees.model.CsvDataSource;
import com.dimtar.garbelyanski.employees.model.DataSource;
import com.dimtar.garbelyanski.employees.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
@Slf4j
@RestController
@RequestMapping("/api/employees")
public class EmployeesController {
    private final EmployeeService employeeService;

    public EmployeesController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @ExceptionHandler(BadFormatException.class)
    public ResponseEntity<String> handleBadFormat(BadCsvFormatException e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAnyError(Exception e){
        log.error("Error!", e);
        return ResponseEntity.internalServerError().body("Internal error!");
    }

    @RequestMapping(method = RequestMethod.POST,path = "/worked-together")
    public ResponseEntity<List<WorkTogetherRecord>> postWorkedTogetherEmployeesFromCSV(@RequestParam(value = "file")MultipartFile csvFile) throws BadFormatException, IOException {
        DataSource dataSource = new CsvDataSource(csvFile);

        return ResponseEntity.status(HttpStatus.OK)
                    .body(this.employeeService.getEmployeesWorkedTogetherFromCSV(dataSource));
    }
}
