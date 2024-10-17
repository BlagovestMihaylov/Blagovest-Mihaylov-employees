package com.sirma.pairs.service;

import com.sirma.pairs.parser.CSVRow;
import com.sirma.pairs.reader.CSVReader;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
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

    public List<TimeWorked> getTopPairDetails(MultipartFile file)
    {
        final List<CSVRow> rows = reader.readFile(file);

        final List<TimeWorked> results = employeeTimeOverlapCalculator.calculateTheMostTimeWorkedTogetherOnOneProject(
                rows);

        return filterTopPairCommonProjects(results);
    }


    @PostConstruct
    public void readFromDevice()
    {
        final List<String> filenames = getFileNames();

        for (String fn : filenames)
        {
            final List<CSVRow> rows = reader.readFile(fn);

            final List<TimeWorked> results = employeeTimeOverlapCalculator.calculateTheMostTimeWorkedTogetherOnOneProject(
                    rows);

            final Optional<TimeWorked> mostTimeTogetherPairOpt = employeeTimeOverlapCalculator.calculateTheMostTimeWorkedTogetherOverall(
                    rows);

            if (mostTimeTogetherPairOpt.isEmpty())
            {
                PrintUtils.printEmpty();
                continue;
            }

            final TimeWorked mostTimeTogetherPair = mostTimeTogetherPairOpt.get();

            final Set<Long> commonProjects = results.stream()
                                                    .filter(r -> topPairFilter(r,
                                                                               mostTimeTogetherPair.firstEmployeeId(),
                                                                               mostTimeTogetherPair.secondEmployeeId()))
                                                    .map(TimeWorked::projectId)
                                                    .collect(Collectors.toSet());

            PrintUtils.printResults(results, fn, mostTimeTogetherPair, commonProjects);
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

    private List<TimeWorked> filterTopPairCommonProjects(List<TimeWorked> results)
    {
        final Optional<TimeWorked> topPairOpt = results.stream()
                                                       .findFirst();

        if (topPairOpt.isEmpty())
        {
            return new ArrayList<>();
        }

        final Long id1 = topPairOpt.get()
                                   .firstEmployeeId();
        final Long id2 = topPairOpt.get()
                                   .secondEmployeeId();


        return results
                .stream()
                .filter(r -> topPairFilter(r, id1, id2))
                .toList();
    }

    private boolean topPairFilter(final TimeWorked tw, Long id1, Long id2)
    {
        return (Objects.equals(tw.firstEmployeeId(), id1) && Objects.equals(tw.secondEmployeeId(), id2))
                || (Objects.equals(tw.firstEmployeeId(), id2) && Objects.equals(tw.secondEmployeeId(), id1));
    }

}

