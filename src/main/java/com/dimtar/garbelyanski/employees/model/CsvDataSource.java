package com.dimtar.garbelyanski.employees.model;

import com.dimtar.garbelyanski.employees.exception.BadCsvFormatException;
import com.dimtar.garbelyanski.employees.util.FileUtil;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class CsvDataSource extends DataSource{

    public CsvDataSource(MultipartFile multipartFile) {
        super(multipartFile);
    }
    @Override
    public List<List<String>> readData() throws BadCsvFormatException, IOException {
        if (!Objects.equals(FilenameUtils.getExtension(this.multipartFile.getOriginalFilename()), "csv")) {
            throw new BadCsvFormatException("Not a csv file!");
        }

        List<List<String>> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(multipartFile.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                records.add(Arrays.stream(values).map(String::strip).toList());
            }
        }
        return records;
    }




}
