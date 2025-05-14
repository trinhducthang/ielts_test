package com.englishtest.englishtest.service;

import com.englishtest.englishtest.entity.Course;
import com.englishtest.englishtest.entity.ReadingTest;
import com.englishtest.englishtest.entity.User;
import com.englishtest.englishtest.repository.ReadingTestRepository;
import com.englishtest.englishtest.repository.UserRepository;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReadingTestService
{
    private final ReadingTestRepository readingTestRepository;

    private final UserRepository userRepository;

    public ReadingTest SaveHtml(String url) throws IOException {
        Document document = Jsoup.connect(url).get();
        String html = document.html();
        ReadingTest page = new ReadingTest(url, html);
        return readingTestRepository.save(page);
    }

    public ReadingTest SaveHtmlFull(String url) throws IOException {
        Document document = Jsoup.connect(url).get();

        // Get title from the <h2> tag
        String title = document.select("h2").first().text();

        // Get image URL from the <img> tag under the <h2> section
        String urlImage = document.select("h2 + img").attr("src");

        // Store raw HTML
        String html = document.html();

        // Create and set up ReadingTest object
        ReadingTest page = new ReadingTest();
        page.setUrl(url);
        page.setContent(html);
        page.setTitle(title);
        page.setUrlImage(urlImage);

        return readingTestRepository.save(page);
    }



    public ReadingTest GetHtml(Long id) throws IOException {
        return readingTestRepository.findById(id).orElse(null);
    }

    public List<Long> getReadingTestsForUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Course course = user.getCourse();
        List<Long> readingTestIds = new ArrayList<>();
        for(ReadingTest readingTest : course.getReadingTests()) {
            readingTestIds.add(readingTest.getId());
        }
        return readingTestIds;
    }

}
