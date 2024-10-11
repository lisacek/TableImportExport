package com.quant.cons;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class CSVFile {

    private final File file;
    private List<List<String>> data;

    public CSVFile(File file) {
        this.file = file;
    }

    public void loadData() {
        List<List<String>> data = new ArrayList<>();
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            while ((line = br.readLine()) != null) {
                var values = parseLine(line);
                data.add(values);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.data = data;
    }

    private List<String> parseLine(String line) {
        List<String> values = new ArrayList<>();
        var pattern = "\"([^\"]*)\"|(?<=,|^)([^,]*)(?:,|$)";
        var matcher = Pattern.compile(pattern).matcher(line);
        while (matcher.find()) {
            String value = matcher.group(1) == null ? matcher.group(2) : matcher.group(1);
            values.add(value);
        }
        return values;
    }

    public List<List<String>> getData() {
        if (data == null) {
            loadData();
        }

        return data;
    }

    public boolean checkIfFileValid(){
        if(file == null || !file.exists() || !file.isFile()) {
            return false;
        }

        if(!file.getName().toLowerCase().endsWith(".csv")) {
            return false;
        }

        try {
            var fileData = new String(Files.readAllBytes(file.toPath()));
            var csvPattern = "(\"[^\"]+\"|[^,]+)(,(\"[^\"]+\"|[^,]+))*\r?\n?";

            for (var line : fileData.split("\n")) {
                if(!Pattern.compile(csvPattern).matcher(line).matches()) {
                    return false;
                }
            }

            return true;
        } catch (IOException e) {
            return false;
        }
    }

}
