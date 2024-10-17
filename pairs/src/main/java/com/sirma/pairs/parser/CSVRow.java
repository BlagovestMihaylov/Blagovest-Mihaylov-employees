package com.sirma.pairs.parser;

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
