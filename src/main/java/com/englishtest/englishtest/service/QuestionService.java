package com.englishtest.englishtest.service;

import com.englishtest.englishtest.dto.QuestionDTO;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.io.IOException;
import java.util.*;

@Service
public class QuestionService {

    public List<QuestionDTO> parseQuestionsFromUrl(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        List<QuestionDTO> list = new ArrayList<>();

        // Lấy các thẻ <p> chứa <select>
        Elements selectQuestions = doc.select("p:has(select)");
        for (Element p : selectQuestions) {
            Element select = p.selectFirst("select");
            if (select != null) {
                String id = select.id();
                String text = p.text();
                list.add(new QuestionDTO(id, text));
            }
        }

        return list;
    }

    private final RestTemplate restTemplate;

    public QuestionService() {
        this.restTemplate = new RestTemplate();
    }

    public List<Map<String, Object>> extractQuestionsFromUrl(String url) {
        // Tải HTML từ link
        String html = restTemplate.getForObject(url, String.class);
        return extractQuestions(html);
    }



    private List<Map<String, Object>> extractQuestions(String html) {
        List<Map<String, Object>> questions = new ArrayList<>();
        Document doc = Jsoup.parse(html);
        Elements ps = doc.select("p");

        String currentQuestion = null;
        LinkedHashMap<String, String> currentChoices = new LinkedHashMap<>();
        boolean isFillInBlank = false;

        for (Element p : ps) {
            String text = p.text().trim();

            // Nếu có input type=text => fill in the blank
            if (!p.select("input[type=text]").isEmpty()) {
                if (currentQuestion != null && !currentChoices.isEmpty()) {
                    Map<String, Object> q = new LinkedHashMap<>();
                    q.put("question", currentQuestion);
                    q.put("choices", new LinkedHashMap<>(currentChoices));
                    q.put("type", isFillInBlank ? "fill-in-the-blank" : "multiple-choice");
                    questions.add(q);
                    currentChoices.clear();
                }
                currentQuestion = text;
                isFillInBlank = true;
            }
            // Nếu có input type=radio => multiple choice
            else if (!p.select("input[type=radio]").isEmpty()) {
                Elements radios = p.select("input[type=radio]");
                for (Element radio : radios) {
                    String label = "";
                    Node next = radio.nextSibling();
                    if (next instanceof TextNode) {
                        label = ((TextNode) next).text().trim();
                    }
                    if (label.isEmpty()) {
                        label = p.text().replaceAll("^\\s*", "").trim();
                    }
                    String value = radio.hasAttr("value") ? radio.attr("value").trim() : "";
                    if (!value.isEmpty() && !label.isEmpty()) {
                        currentChoices.put(value, label);
                    }
                }
                isFillInBlank = false;
            }
            // Nếu text bắt đầu số thứ tự
            else if (text.matches("^\\d+\\.\\s?.+")) {
                if (currentQuestion != null && !currentChoices.isEmpty()) {
                    Map<String, Object> q = new LinkedHashMap<>();
                    q.put("question", currentQuestion);
                    q.put("choices", new LinkedHashMap<>(currentChoices));
                    q.put("type", isFillInBlank ? "fill-in-the-blank" : "multiple-choice");
                    questions.add(q);
                    currentChoices.clear();
                }
                currentQuestion = text;
                isFillInBlank = false;
            }
        }

        // Lưu câu hỏi cuối cùng nếu còn
        if (currentQuestion != null) {
            Map<String, Object> q = new LinkedHashMap<>();
            q.put("question", currentQuestion);
            if (!currentChoices.isEmpty()) {
                q.put("choices", currentChoices);
            }
            q.put("type", isFillInBlank ? "fill-in-the-blank" : "multiple-choice");
            questions.add(q);
        }

        return questions;
    }



    public String fetchHtmlContent(String url) throws IOException {
        Document document = Jsoup.connect(url).get();
        return document.html();
    }


}
