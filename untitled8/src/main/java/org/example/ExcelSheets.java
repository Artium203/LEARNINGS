package org.example;

import org.apache.poi.ss.usermodel.Sheet;

import java.util.List;

public class ExcelSheets {
        private Sheet sheet;
        private List<String> column1;

    public ExcelSheets(Sheet sheet) {
        this.sheet = sheet;
    }
}
