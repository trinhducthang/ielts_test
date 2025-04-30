package com.englishtest.englishtest.controller;

import com.englishtest.englishtest.dto.QuestionDTO;
import com.englishtest.englishtest.entity.ReadingTest;
import com.englishtest.englishtest.service.QuestionService;
import com.englishtest.englishtest.service.ReadingTestService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    private final QuestionService service;
    private final ReadingTestService readingTestService;

    public QuestionController(QuestionService service, ReadingTestService readingTestService) {
        this.service = service;
        this.readingTestService = readingTestService;
    }


    @GetMapping("/full")
    public String fetchHtml(@RequestParam String url) throws Exception {
        return service.fetchHtmlContent(url);
    }

    @PostMapping
    public ReadingTest fetchAndStore(@RequestParam String url) throws IOException {
        return readingTestService.SaveHtml(url);
    }



}
