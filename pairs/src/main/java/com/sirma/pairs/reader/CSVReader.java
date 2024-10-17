package com.sirma.pairs.reader;

import com.sirma.pairs.parser.CSVParser;
import com.sirma.pairs.parser.CSVRow;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
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


    public List<CSVRow> readFile(MultipartFile file)
    {

        try
        {
            BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()));
            return getCsvRows(br);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }

    }

    public List<CSVRow> readFile(String filePath)
    {

        try
        {
            BufferedReader br = new BufferedReader(new FileReader(filePath));

            return getCsvRows(br);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }

    }

    private List<CSVRow> getCsvRows(BufferedReader br)
    throws IOException
    {
        List<CSVRow> rows = new ArrayList<>();
        String line;
        String delimiter = ",";

         br.readLine();  // Skip the first line

            while ((line = br.readLine()) != null)
            {
                String[] values = line.split(delimiter);
                CSVRow row = parser.parseCSVRow(values);
                rows.add(row);
            }


        return rows;
    }
}
