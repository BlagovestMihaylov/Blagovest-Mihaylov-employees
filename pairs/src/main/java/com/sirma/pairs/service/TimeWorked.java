package com.sirma.pairs.service;

import java.time.LocalDate;

public record TimeWorked
        (
                Long firstEmployeeId,
                Long secondEmployeeId,
                Long projectId,
                LocalDate dateFrom,
                LocalDate dateTo,
                int daysWorkedTogether
        )
{
}
