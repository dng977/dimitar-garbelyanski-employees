package com.dimtar.garbelyanski.employees.service;

import com.dimtar.garbelyanski.employees.dto.WorkTogetherRecord;
import com.dimtar.garbelyanski.employees.exception.BadCsvFormatException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface EmployeeService {
    List<WorkTogetherRecord> getEmployeesWorkedTogether(MultipartFile csvFile) throws IOException, BadCsvFormatException;


}
