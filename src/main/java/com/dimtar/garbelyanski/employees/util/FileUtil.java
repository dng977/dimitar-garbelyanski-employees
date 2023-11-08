package com.dimtar.garbelyanski.employees.util;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileUtil {
    public static List<List<String>> scanProjectsData(InputStream fileInputStream){
        List<List<String>> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(fileInputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                records.add(Arrays.stream(values).map(String::strip).toList());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return records;
    }
}
