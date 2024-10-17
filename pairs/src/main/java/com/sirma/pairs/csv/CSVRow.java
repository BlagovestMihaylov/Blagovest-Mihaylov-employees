package com.sirma.pairs.csv;

import java.time.LocalDate;

public record CSVRow
        (
                Integer employeeId,
                Integer projectId,
                LocalDate dateFrom,
                LocalDate dateTo
        )
{
}
