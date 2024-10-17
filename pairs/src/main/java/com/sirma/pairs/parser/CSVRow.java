package com.sirma.pairs.parser;

import java.time.LocalDate;

public record CSVRow
        (
                Long employeeId,
                Long projectId,
                LocalDate dateFrom,
                LocalDate dateTo
        )
{
}
