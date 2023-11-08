package com.dimtar.garbelyanski.employees.controller;

import com.dimtar.garbelyanski.employees.dto.WorkTogetherRecord;
import com.dimtar.garbelyanski.employees.exception.BadCsvFormatException;
import com.dimtar.garbelyanski.employees.service.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeesController {
    private final EmployeeService employeeService;

    public EmployeesController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }


    @RequestMapping(method = RequestMethod.POST,path = "/worked-together")
    public ResponseEntity<List<WorkTogetherRecord>> postWorkedTogetherEmployees(@RequestParam(value = "file")MultipartFile file){
        try{
            return ResponseEntity.status(HttpStatus.OK)
                    .body(this.employeeService.getEmployeesWorkedTogether(file));
        }catch (IOException |BadCsvFormatException e){
            return ResponseEntity.badRequest().build();
        }
    }
}
