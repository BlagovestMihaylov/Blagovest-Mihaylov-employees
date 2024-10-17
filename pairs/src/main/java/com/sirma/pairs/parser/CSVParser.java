package com.sirma.pairs.parser;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;

@Service
public class CSVParser
{

    private final DateTimeFormatter flexibleFormatter = new DateTimeFormatterBuilder()
            .appendOptional(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            .appendOptional(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            .appendOptional(DateTimeFormatter.ofPattern("MM-dd-yyyy"))
            .appendOptional(DateTimeFormatter.ofPattern("yyyy.MM.dd"))
            .appendOptional(DateTimeFormatter.ofPattern("MMM d, yyyy"))
            .appendOptional(DateTimeFormatter.ofPattern("d MMM yyyy"))
            .appendOptional(DateTimeFormatter.ofPattern("d-MMM-yyyy"))
            .appendOptional(DateTimeFormatter.ofPattern("yyyy/MM/dd"))
            .appendOptional(new DateTimeFormatterBuilder()
                                    .appendPattern("yyyy")
                                    .optionalStart()
                                    .appendPattern("-MM")
                                    .optionalEnd()
                                    .optionalStart()
                                    .appendPattern("-dd")
                                    .optionalEnd()
                                    .parseDefaulting(ChronoField.MONTH_OF_YEAR, 1)
                                    .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
                                    .toFormatter())  //handles partial dates like "2024" or "2024-10"
            .toFormatter();

    public CSVRow parseCSVRow(String[] values)
    {
        return new CSVRow(
                Integer.parseInt(values[0]),
                Integer.parseInt(values[1]),
                parseDate(values[2]),
                parseDate(values[3])
        );
    }

    private LocalDate parseDate(final String input)
    {

        if (input.equalsIgnoreCase("NULL"))
        {
            return LocalDate.now();
        }


        try
        {
            return LocalDate.parse(input, flexibleFormatter);
        }
        catch (DateTimeParseException e)
        {
            try
            {
                return LocalDate.parse(input, DateTimeFormatter.ofPattern("dd-MM-yyyy")); //MM-dd-yyyy interferes
            }
            catch (DateTimeParseException dte)
            {
                System.err.println("Failed to parse date: " + input + " with error: " + e.getMessage());
                throw new IllegalArgumentException("Date format not supported: " + input);
            }

        }
    }
}