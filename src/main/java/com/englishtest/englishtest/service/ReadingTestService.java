package com.englishtest.englishtest.service;

import com.englishtest.englishtest.entity.ReadingTest;
import com.englishtest.englishtest.repository.ReadingTestRepository;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ReadingTestService
{
    private final ReadingTestRepository readingTestRepository;

    public ReadingTest SaveHtml(String url) throws IOException {
        Document document = Jsoup.connect(url).get();
        String html = document.html();
        ReadingTest page = new ReadingTest(url, html);
        return readingTestRepository.save(page);
    }

}
