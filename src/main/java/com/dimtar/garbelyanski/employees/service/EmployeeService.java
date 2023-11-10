package com.dimtar.garbelyanski.employees.service;

import com.dimtar.garbelyanski.employees.dto.WorkTogetherRecord;
import com.dimtar.garbelyanski.employees.exception.BadCsvFormatException;
import com.dimtar.garbelyanski.employees.exception.BadFormatException;
import com.dimtar.garbelyanski.employees.model.DataSource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface EmployeeService {
    List<WorkTogetherRecord> getEmployeesWorkedTogetherFromCSV(DataSource dataSource) throws IOException, BadFormatException;


}
