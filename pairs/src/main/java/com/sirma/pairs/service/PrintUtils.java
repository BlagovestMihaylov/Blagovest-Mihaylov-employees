package com.sirma.pairs.service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class PrintUtils
{

    public static void printEmpty()
    {
        System.out.println("No employee pairs found.");
    }

    public static void printResults(final List<TimeWorked> results,
                                    final String filename,
                                    final TimeWorked mostWorkedTogetherPair,
                                    final Set<Long> commonProjects)
    {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        System.out.printf("%n=== Results for File: %s ===%n%n", filename);

        final Optional<TimeWorked> topPairOpt = results.stream()
                                                       .findFirst();

        if (topPairOpt.isEmpty())
        {
            printEmpty();
            return;
        }

        final TimeWorked topPair = topPairOpt.get();

        System.out.printf(
                ">>> Pair That Worked Together the Most: %d and %d on projects %s for an overall of %d %s.%n%n",
                mostWorkedTogetherPair.firstEmployeeId(),
                mostWorkedTogetherPair.secondEmployeeId(),
                commonProjects.toString(),
                mostWorkedTogetherPair.daysWorkedTogether(),
                mostWorkedTogetherPair.daysWorkedTogether() == 1 ? "day" : "days");


        System.out.println(">>> Pair That Worked Together on a single project the Most:");
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
