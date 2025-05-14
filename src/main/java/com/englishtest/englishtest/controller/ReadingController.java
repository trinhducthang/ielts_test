package com.englishtest.englishtest.controller;


import com.englishtest.englishtest.dto.ReadingTestDTO;
import com.englishtest.englishtest.entity.ReadingTest;
import com.englishtest.englishtest.repository.ReadingTestRepository;
import com.englishtest.englishtest.service.ReadingTestService;
import org.jsoup.select.Elements;
import org.springframework.data.domain.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
@RestController
@RequestMapping("/api/reading")
public class ReadingController {

    private final ReadingTestService readingTestService;

    private final ReadingTestRepository readingTestRepository;

    public ReadingController(ReadingTestService readingTestService, ReadingTestRepository readingTestRepository) {
        this.readingTestService = readingTestService;
        this.readingTestRepository = readingTestRepository;
    }

    @PostMapping()
    public ReadingTest getReadingTest(@RequestParam String url) throws IOException {
        return readingTestService.SaveHtmlFull(url);
    }

    @GetMapping("/content/{id}")
    public ResponseEntity<String> renderReadingTest(@PathVariable Long id) {
        Optional<ReadingTest> readingTestOpt = readingTestRepository.findById(id);
        if (readingTestOpt.isPresent()) {
            ReadingTest test = readingTestOpt.get();
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, "text/html; charset=UTF-8")
                    .body(test.getContent());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("")
    public ResponseEntity<Page<ReadingTestDTO>> getReadingTestsPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<ReadingTest> pageResult = readingTestRepository.findAll(pageable);

        Page<ReadingTestDTO> dtoPage = pageResult.map(test -> {
            ReadingTestDTO dto = new ReadingTestDTO();
            dto.setTitle(test.getTitle());
            dto.setUrlImage(test.getUrlImage());
            dto.setId(test.getId()); // để link tới chi tiết nếu cần
            return dto;
        });

        return ResponseEntity.ok(dtoPage);
    }

    @GetMapping("/by-ids")
    public ResponseEntity<Page<ReadingTestDTO>> getReadingTestsByIds(
            @RequestParam List<Long> ids,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<ReadingTest> pageResult = readingTestRepository.findByIdIn(ids, pageable);

        Page<ReadingTestDTO> dtoPage = pageResult.map(test -> {
            ReadingTestDTO dto = new ReadingTestDTO();
            dto.setTitle(test.getTitle());
            dto.setUrlImage(test.getUrlImage());
            dto.setId(test.getId());
            return dto;
        });

        return ResponseEntity.ok(dtoPage);
    }


    @GetMapping("/get-solution-link")
    public String getSolutionLink(@RequestParam String url) {
        try {
            // Kết nối và lấy nội dung HTML từ URL người dùng cung cấp
            Document doc = Jsoup.connect(url).get();

            // Tìm thẻ <a> có class btn-solution
            Element aTag = doc.selectFirst("a.btn-solution");

            if (aTag != null) {
                // Lấy href gốc từ thẻ <a>
                String href = aTag.attr("href");
                return "https://mini-ielts.com" + href ;
            } else {
                return "Không tìm thấy thẻ <a class='btn-solution'> trong trang.";
            }

        } catch (Exception e) {
            return "Lỗi: " + e.getMessage();
        }
    }

    @GetMapping("/get-answers")
    public Map<String, String> getAnswers(@RequestParam String url) {
        Map<String, String> answers = new LinkedHashMap<>(); // Giữ thứ tự câu hỏi

        try {
            // Kết nối tới trang
            Document doc = Jsoup.connect(url).get();

            // Lấy đúng bảng chứa đáp án
            Element table = doc.selectFirst("table.table.table-bordered.table-condensed.text-left");

            if (table != null) {
                Elements tds = table.select("td");

                for (Element td : tds) {
                    Element bTag = td.selectFirst("b");
                    if (bTag != null) {
                        String questionNumber = bTag.text().replace(".", "").trim(); // "1." -> "1"
                        String fullText = td.text(); // "1. YES"
                        String answerText = fullText.replaceFirst("\\d+\\.\\s*", "").trim(); // "YES"

                        answers.put(questionNumber, answerText);
                    }
                }
            }
        } catch (Exception e) {
            answers.put("error", e.getMessage());
        }

        return answers;
    }



    @GetMapping("/getByUserId/{userId}")
    public List<Long> getReadingTestsForUser(@PathVariable Long userId){
        return readingTestService.getReadingTestsForUser(userId);
    }


}
