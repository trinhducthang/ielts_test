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



    private final RestTemplate restTemplate;

    public QuestionService() {
        this.restTemplate = new RestTemplate();
    }

    public String fetchHtmlContent(String url) throws IOException {
        Document document = Jsoup.connect(url).get();
        return document.html();
    }



}
