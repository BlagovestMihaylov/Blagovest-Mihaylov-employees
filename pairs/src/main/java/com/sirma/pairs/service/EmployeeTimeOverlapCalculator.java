package com.sirma.pairs.service;

import com.sirma.pairs.parser.CSVRow;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class EmployeeTimeOverlapCalculator
{

    // Event class to represent a start or end of an employee's work period
    private static class Event
    {
        Long employeeId;
        LocalDate date;
        boolean isStart; // true for start, false for end

        Event(Long employeeId, LocalDate date, boolean isStart)
        {
            this.employeeId = employeeId;
            this.date = date;
            this.isStart = isStart;
        }
    }

    public List<TimeWorked> getTimeWorkedSortedByDuration(List<CSVRow> rows)
    {
        List<TimeWorked> timeWorkedList = calculateTimeWorkedTogether(rows);
        timeWorkedList.sort(Comparator.comparingLong(TimeWorked::daysWorkedTogether)
                                      .reversed());
        return timeWorkedList;
    }

    public List<TimeWorked> calculateTimeWorkedTogether(List<CSVRow> rows)
    {
        List<Event> events = generateEvents(rows);
        return processEvents(events, rows);
    }

    private List<Event> generateEvents(List<CSVRow> rows)
    {
        List<Event> events = new ArrayList<>();
        for (CSVRow row : rows)
        {
            events.add(new Event(row.employeeId(), row.dateFrom(), true));  // Start event
            LocalDate dateTo = (row.dateTo() == null) ? LocalDate.now() : row.dateTo();
            events.add(new Event(row.employeeId(), dateTo, false));        // End event
        }

        events.sort(Comparator.comparing((Event e) -> e.date)
                              .thenComparing(e -> !e.isStart));
        return events;
    }

    private List<TimeWorked> processEvents(List<Event> events, List<CSVRow> rows)
    {
        List<TimeWorked> result = new ArrayList<>();
        Set<String> processedPairs = new HashSet<>(); // Set to track processed pairs
        Map<Long, List<Long>> activeEmployeesByProject = new HashMap<>();
        Map<Long, CSVRow> rowMap = new HashMap<>();

        for (CSVRow row : rows)
        {
            rowMap.put(row.employeeId(), row);
        }

        for (Event event : events)
        {
            CSVRow currentRow = rowMap.get(event.employeeId);

            if (event.isStart)
            {
                activeEmployeesByProject
                        .computeIfAbsent(currentRow.projectId(), _ -> new ArrayList<>())
                        .add(event.employeeId);

                List<Long> activeEmployees = activeEmployeesByProject.get(currentRow.projectId());

                for (Long activeEmployeeId : activeEmployees)
                {
                    if (!activeEmployeeId.equals(event.employeeId))
                    {
                        String pairKey = createPairKey(activeEmployeeId, event.employeeId);

                        if (!processedPairs.contains(pairKey))
                        {
                            CSVRow activeRow = rowMap.get(activeEmployeeId);
                            LocalDate maxStartDate = calculateMaxStartDate(currentRow, activeRow);
                            LocalDate minEndDate = calculateMinEndDate(currentRow, activeRow);

                            if (!maxStartDate.isAfter(minEndDate))
                            {
                                int daysWorkedTogether = (int) (ChronoUnit.DAYS.between(maxStartDate, minEndDate) + 1);
                                result.add(new TimeWorked(
                                        currentRow.employeeId(),
                                        activeRow.employeeId(),
                                        currentRow.projectId(),
                                        maxStartDate,
                                        minEndDate,
                                        daysWorkedTogether
                                ));

                            }

                            processedPairs.add(pairKey);
                        }
                    }
                }
            } else
            {
                // On end event, remove the employee from their project's active set
                List<Long> activeEmployees = activeEmployeesByProject.get(currentRow.projectId());
                if (activeEmployees != null)
                {
                    activeEmployees.remove(event.employeeId);
                    if (activeEmployees.isEmpty())
                    {
                        activeEmployeesByProject.remove(currentRow.projectId());
                    }
                }
            }
        }
        return result;
    }

    private String createPairKey(Long id1, Long id2)
    {
        return id1 < id2 ? id1 + "-" + id2 : id2 + "-" + id1;
    }

    private static LocalDate calculateMaxStartDate(CSVRow currentRow, CSVRow activeRow)
    {
        return currentRow.dateFrom()
                         .isAfter(activeRow.dateFrom()) ? currentRow.dateFrom() : activeRow.dateFrom();
    }

    private static LocalDate calculateMinEndDate(CSVRow currentRow, CSVRow activeRow)
    {
        return currentRow.dateTo() != null && activeRow.dateTo() != null ?
                (currentRow.dateTo()
                           .isBefore(activeRow.dateTo()) ? currentRow.dateTo() : activeRow.dateTo()) :
                LocalDate.now();
    }
}
