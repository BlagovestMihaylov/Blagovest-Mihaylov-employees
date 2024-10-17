package com.sirma.pairs.service;

import com.sirma.pairs.parser.CSVParser;
import com.sirma.pairs.reader.CSVReader;
import org.springframework.stereotype.Service;

@Service
public class CSVService
{
    private final EmployeeTimeOverlapCalculator employeeTimeOverlapCalculator;
    private final CSVReader reader;
    private final CSVParser parser;

    public CSVService(EmployeeTimeOverlapCalculator employeeTimeOverlapCalculator, CSVReader reader, CSVParser parser)
    {
        this.employeeTimeOverlapCalculator = employeeTimeOverlapCalculator;
        this.reader = reader;
        this.parser = parser;
    }

}

