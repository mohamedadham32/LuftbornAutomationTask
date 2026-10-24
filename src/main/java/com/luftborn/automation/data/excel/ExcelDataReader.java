package com.luftborn.automation.data.excel;

import com.luftborn.automation.data.DataAccessException;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class ExcelDataReader {
    private ExcelDataReader() {}

    public static List<Map<String, String>> read(Path path, String sheetName) {
        try (InputStream stream = Files.newInputStream(path); Workbook workbook = WorkbookFactory.create(stream)) {
            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) throw new DataAccessException("Sheet not found: " + sheetName);
            return rows(sheet);
        } catch (IOException e) { throw new DataAccessException("Could not read workbook: " + path, e); }
    }

    public static Object[][] asDataProvider(Path path, String sheetName) {
        List<Map<String, String>> data = read(path, sheetName);
        Object[][] result = new Object[data.size()][1];
        for (int i = 0; i < data.size(); i++) result[i][0] = data.get(i);
        return result;
    }

    private static List<Map<String, String>> rows(Sheet sheet) {
        DataFormatter formatter = new DataFormatter();
        Row header = sheet.getRow(sheet.getFirstRowNum());
        if (header == null) return List.of();
        List<String> keys = new ArrayList<>();
        for (int c = 0; c < header.getLastCellNum(); c++) keys.add(formatter.formatCellValue(header.getCell(c)).trim());
        List<Map<String, String>> result = new ArrayList<>();
        for (int r = header.getRowNum() + 1; r <= sheet.getLastRowNum(); r++) {
            Row row = sheet.getRow(r);
            if (row == null) continue;
            Map<String, String> values = new LinkedHashMap<>();
            for (int c = 0; c < keys.size(); c++) values.put(keys.get(c), formatter.formatCellValue(row.getCell(c)));
            result.add(values);
        }
        return List.copyOf(result);
    }
}
