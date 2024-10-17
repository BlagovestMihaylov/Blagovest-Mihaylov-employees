package com.sirma.pairs.service;

import java.time.LocalDate;

public record TimeWorked
        (
                Integer firstEmployeeId,
                Integer secondEmployeeId,
                Integer projectId,
                LocalDate dateFrom,
                LocalDate dateTo,
                int daysWorkedTogether
        )
{
}
