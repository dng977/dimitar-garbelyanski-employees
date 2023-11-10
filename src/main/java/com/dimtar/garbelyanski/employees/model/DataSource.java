package com.dimtar.garbelyanski.employees.model;

import com.dimtar.garbelyanski.employees.exception.BadCsvFormatException;
import com.dimtar.garbelyanski.employees.exception.BadFormatException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

public abstract class DataSource {

    protected MultipartFile multipartFile;

    public DataSource(MultipartFile multipartFile) {
        this.multipartFile = multipartFile;
    }

    public abstract List<List<String>> readData() throws BadFormatException, IOException;
}
