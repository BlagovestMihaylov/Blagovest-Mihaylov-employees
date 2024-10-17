package com.sirma.pairs.service;

import com.sirma.pairs.parser.CSVRow;
import com.sirma.pairs.reader.CSVReader;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class CSVService
{
    private static final String CSV_FOLDER_PATH = "..\\project_data";

    private final EmployeeTimeOverlapCalculator employeeTimeOverlapCalculator;
    private final CSVReader reader;


    public CSVService(EmployeeTimeOverlapCalculator employeeTimeOverlapCalculator, CSVReader reader)
    {
        this.employeeTimeOverlapCalculator = employeeTimeOverlapCalculator;
        this.reader = reader;
    }


    @PostConstruct
    public void readFromDevice()
    {

        final List<String> filenames = getFileNames();


        for (String fn : filenames)
        {
            final List<CSVRow> rows = reader.readFile(fn);

            final List<TimeWorked> results = employeeTimeOverlapCalculator.getTimeWorkedSortedByDuration(rows);

            printResults(results, fn);
        }


    }


    private List<String> getFileNames()
    {
        try (Stream<Path> paths = Files.walk(Paths.get(CSV_FOLDER_PATH)))
        {
            return paths
                    .filter(Files::isRegularFile)
                    .map(Path::toString)
                    .toList();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }


    private void printResults(List<TimeWorked> results, String filename)
    {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        System.out.printf("%n=== Results for File: %s ===%n%n", filename);

        final Optional<TimeWorked> topPairOpt = results.stream()
                                                       .findFirst();

        if (topPairOpt.isEmpty())
        {
            System.out.println("No employee pairs found.");
            return;
        }

        final TimeWorked topPair = topPairOpt.get();

        System.out.println(">>> Pair That Worked Together the Most:");
        System.out.printf("Employee %d worked with employee %d on project %d from %s to %s for %d %s.%n%n",
                          topPair.firstEmployeeId(),
                          topPair.secondEmployeeId(),
                          topPair.projectId(),
                          topPair.dateFrom()
                                 .format(dateFormatter),
                          topPair.dateTo()
                                 .format(dateFormatter),
                          topPair.daysWorkedTogether(),
                          topPair.daysWorkedTogether() == 1 ? "day" : "days");

        System.out.println(">>> All Employee Pairs:");
        System.out.printf("%-15s %-15s %-15s %-15s %-15s %-10s%n", "Employee 1", "Employee 2", "Project ID",
                          "Date From", "Date To", "Days");

        for (TimeWorked tw : results)
        {
            System.out.printf("%-15d %-15d %-15d %-15s %-15s %-10d%n",
                              tw.firstEmployeeId(),
                              tw.secondEmployeeId(),
                              tw.projectId(),
                              tw.dateFrom()
                                .format(dateFormatter),
                              tw.dateTo()
                                .format(dateFormatter),
                              tw.daysWorkedTogether());
        }

        System.out.println();
    }

}

