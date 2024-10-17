package com.sirma.pairs.parser;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class CSVParserTest
{
    private final CSVParser parser = new CSVParser();

    @Test
    void parseCSVRow_ValidData_ddMMyyyy() {
        String[] values = {"192", "6", "04/04/2002", "13/08/2003"};
        CSVRow row = parser.parseCSVRow(values);

        assertEquals(192, row.employeeId());
        assertEquals(6, row.projectId());
        assertEquals(LocalDate.of(2002, 4, 4), row.dateFrom());
        assertEquals(LocalDate.of(2003, 8, 13), row.dateTo());
    }

    @Test
    void parseCSVRow_ValidData_MMddyyyy() {
        String[] values = {"291", "8", "11-06-2002", "11-06-2004"};
        CSVRow row = parser.parseCSVRow(values);

        assertEquals(291, row.employeeId());
        assertEquals(8, row.projectId());
        assertEquals(LocalDate.of(2002, 11, 6), row.dateFrom());
        assertEquals(LocalDate.of(2004, 11, 6), row.dateTo());
    }

    @Test
    void parseCSVRow_InvalidDateFormat() {
        String[] values = {"192", "6", "invalid-date", "13/08/2003"};
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            parser.parseCSVRow(values);
        });

        assertEquals("Date format not supported: invalid-date", exception.getMessage());
    }

    @Test
    void parseCSVRow_MissingDate() {
        String[] values = {"291", "8", "", "11-06-2004"};
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            parser.parseCSVRow(values);
        });

        assertEquals("Date format not supported: ", exception.getMessage());
    }

    @Test
    void parseCSVRow_PartialDate() {
        String[] values = {"106", "13", "2024", "11-07-2022"};
        CSVRow row = parser.parseCSVRow(values);

        assertEquals(106, row.employeeId());
        assertEquals(13, row.projectId());
        assertEquals(LocalDate.of(2024, 1, 1), row.dateFrom()); // Defaults to January 1st
        assertEquals(LocalDate.of(2022, 11, 7), row.dateTo());
    }

    @Test
    void parseCSVRow_ValidData_MixedFormats() {
        String[] values = {"176", "9", "2003-04-17", "30-03-2007"};
        CSVRow row = parser.parseCSVRow(values);

        assertEquals(176, row.employeeId());
        assertEquals(9, row.projectId());
        assertEquals(LocalDate.of(2003, 4, 17), row.dateFrom());
        assertEquals(LocalDate.of(2007, 3, 30), row.dateTo());
    }
}