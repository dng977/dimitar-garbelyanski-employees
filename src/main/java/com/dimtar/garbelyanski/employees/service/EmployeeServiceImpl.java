package com.dimtar.garbelyanski.employees.service;

import com.dimtar.garbelyanski.employees.dto.WorkTogetherRecord;
import com.dimtar.garbelyanski.employees.exception.BadCsvFormatException;
import com.dimtar.garbelyanski.employees.exception.BadFormatException;
import com.dimtar.garbelyanski.employees.model.CsvDataSource;
import com.dimtar.garbelyanski.employees.model.DataSource;
import com.dimtar.garbelyanski.employees.model.EmployeeWorkRecord;
import com.dimtar.garbelyanski.employees.util.FileUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    public List<WorkTogetherRecord> getEmployeesWorkedTogetherFromCSV(DataSource dataSource) throws IOException, BadFormatException {
        return WorkTogetherRecord.calculateWorkTogetherRecords(EmployeeWorkRecord.buildFromDataSource(dataSource));
    }


}



