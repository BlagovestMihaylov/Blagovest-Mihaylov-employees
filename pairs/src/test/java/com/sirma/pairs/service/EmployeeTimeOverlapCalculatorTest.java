package com.sirma.pairs.service;

import com.sirma.pairs.parser.CSVRow;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeTimeOverlapCalculatorTest
{

    private final EmployeeTimeOverlapCalculator calculator = new EmployeeTimeOverlapCalculator();

    @Test
    void calculateTimeWorkedTogether()
    {
        List<CSVRow> rows = List.of(
                // Project 101
                new CSVRow(1L, 101L, LocalDate.of(2023, 1, 1), LocalDate.of(2023, 1, 10)), // Employee 1
                new CSVRow(2L, 101L, LocalDate.of(2023, 1, 5), LocalDate.of(2023, 1, 15)), // Employee 2

                // Project 103
                new CSVRow(3L, 103L, LocalDate.of(2023, 1, 15), LocalDate.of(2023, 1, 20)), // Employee 3
                new CSVRow(5L, 103L, LocalDate.of(2023, 1, 19), LocalDate.of(2023, 1, 22)), // Employee 5
                new CSVRow(6L, 103L, LocalDate.of(2023, 1, 20), LocalDate.of(2023, 1, 25))  // Employee 6
        );

        List<TimeWorked> timeWorkedTogether = calculator.calculateTimeWorkedTogether(rows);

        assertFalse(timeWorkedTogether.isEmpty(), "Expected non-empty list of time worked together");

        assertEquals(4, timeWorkedTogether.size());


        final Set<Integer> daysWorked = Set.of(6,3,2,1);

        for (TimeWorked tw : timeWorkedTogether)
        {
            assertTrue(daysWorked.contains(tw.daysWorkedTogether()));
        }


    }

    @Test
    void calculateTheMostTimeWorkedTogetherOnOneProject()
    {
        List<CSVRow> rows = List.of(
                new CSVRow(253L, 15L, LocalDate.of(2017, 9, 17), LocalDate.of(2021, 8, 31)), // Employee 1
                new CSVRow(298L, 12L, LocalDate.of(2013, 12, 25), LocalDate.of(2019, 8, 14)), // Employee 2
                new CSVRow(213L, 19L, LocalDate.of(2004, 9, 18), LocalDate.of(2015, 1, 8)), // Employee 3
                new CSVRow(104L, 12L, LocalDate.of(2012, 12, 8), LocalDate.of(2019, 1, 1)), // Employee 4
                new CSVRow(177L, 19L, LocalDate.of(2022, 7, 6), LocalDate.of(2023, 1, 1))  // Employee 6
        );

        List<TimeWorked> sortedTimeWorked = calculator.calculateTheMostTimeWorkedTogetherOnOneProject(rows);

        assertNotNull(sortedTimeWorked);
        assertFalse(sortedTimeWorked.isEmpty());

        for (int i = 1;
             i < sortedTimeWorked.size();
             i++)
        {
            assertTrue(sortedTimeWorked.get(i - 1)
                                       .daysWorkedTogether() >= sortedTimeWorked.get(i)
                                                                                .daysWorkedTogether());
        }
    }
}
