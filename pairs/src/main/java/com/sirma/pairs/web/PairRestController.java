package com.sirma.pairs.web;

import com.sirma.pairs.service.CSVService;
import com.sirma.pairs.service.TimeWorked;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/upload")
public class PairRestController
{

    private final CSVService service;

    public PairRestController(CSVService service)
    {
        this.service = service;
    }

    @PostMapping
    public List<TimeWorked> getTopPairDetails(MultipartFile file)
    {
        if (file.isEmpty())
        {
           throw new RuntimeException("Empty file");
        }

       return service.getTopPairDetails(file);
    }
}
