package com.luftborn.automation.data.excel;

import com.luftborn.automation.data.DataAccessException;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public final class ExcelDataWriter {
    private ExcelDataWriter() {}
    public static void write(Path path, String sheetName, List<Map<String, ?>> records) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet(sheetName);
            if (!records.isEmpty()) {
                List<String> headers = List.copyOf(records.get(0).keySet());
                Row header = sheet.createRow(0);
                for (int i = 0; i < headers.size(); i++) header.createCell(i).setCellValue(headers.get(i));
                for (int r = 0; r < records.size(); r++) {
                    Row row = sheet.createRow(r + 1);
                    for (int c = 0; c < headers.size(); c++) {
                        Object value = records.get(r).get(headers.get(c));
                        row.createCell(c).setCellValue(value == null ? "" : String.valueOf(value));
                    }
                }
                for (int i = 0; i < headers.size(); i++) sheet.autoSizeColumn(i);
            }
            Path parent = path.toAbsolutePath().getParent();
            if (parent != null) Files.createDirectories(parent);
            try (OutputStream output = Files.newOutputStream(path)) { workbook.write(output); }
        } catch (IOException e) { throw new DataAccessException("Could not write workbook: " + path, e); }
    }
}
