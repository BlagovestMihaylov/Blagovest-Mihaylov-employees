package com.sirma.pairs.reader;

import com.sirma.pairs.parser.CSVParser;
import com.sirma.pairs.parser.CSVRow;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CSVReader
{

    private final CSVParser parser;

    public CSVReader(CSVParser parser)
    {
        this.parser = parser;
    }


    public List<CSVRow> readFile(String filePath)
    {
        List<CSVRow> rows = new ArrayList<>();
        String line;
        String delimiter = ",";

        try (BufferedReader br = new BufferedReader(new FileReader(filePath)))
        {
            br.readLine();  // Skip the first line

            while ((line = br.readLine()) != null)
            {
                String[] values = line.split(delimiter);
                CSVRow row = parser.parseCSVRow(values);
                rows.add(row);
            }

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return rows;
    }
}
