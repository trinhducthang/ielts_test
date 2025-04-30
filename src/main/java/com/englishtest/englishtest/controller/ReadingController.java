package com.englishtest.englishtest.controller;

import com.englishtest.englishtest.entity.Passage;
import com.englishtest.englishtest.entity.Question;
import com.englishtest.englishtest.entity.StudentAnswer;
import com.englishtest.englishtest.entity.User;
import com.englishtest.englishtest.repository.PassageRepository;
import com.englishtest.englishtest.repository.QuestionRepository;
import com.englishtest.englishtest.repository.StudentAnswerRepository;
import com.englishtest.englishtest.repository.UserRepository;
import org.springframework.web.bind.annotation.*;
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

    private final PassageRepository passageRepository;
    private final QuestionRepository questionRepository;
    private final StudentAnswerRepository studentAnswerRepository;
    private final UserRepository userRepository;

    public ReadingController(PassageRepository passageRepository,
                                 QuestionRepository questionRepository,
                                 StudentAnswerRepository studentAnswerRepository,
                                 UserRepository userRepository) {
        this.passageRepository = passageRepository;
        this.questionRepository = questionRepository;
        this.studentAnswerRepository = studentAnswerRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/passage/{id}")
    public Map<String, Object> getPassage(@PathVariable Long id) {
        Passage passage = passageRepository.findById(id).orElseThrow();
        List<Question> questions = questionRepository.findByPassageId(id);
        Map<String, Object> response = new HashMap<>();
        response.put("passage", passage);
        response.put("questions", questions);
        return response;
    }

    @PostMapping("/submit")
    public String submitAnswers(@RequestParam Long userId,
                                @RequestParam List<Long> questionIds,
                                @RequestParam List<String> answers) {
        User student = userRepository.findById(userId).orElseThrow();

        for (int i = 0; i < questionIds.size(); i++) {
            Question q = questionRepository.findById(questionIds.get(i)).orElseThrow();
            StudentAnswer ans = new StudentAnswer();
            ans.setUser(student);
            ans.setQuestion(q);
            ans.setAnswerText(answers.get(i));
            ans.setCorrect(q.getCorrectAnswer().equalsIgnoreCase(answers.get(i)));
            studentAnswerRepository.save(ans);
        }
        return "Submitted";
    }

    @GetMapping("/result")
    public List<StudentAnswer> getResults(@RequestParam Long userId) {
        return studentAnswerRepository.findByUserId(userId);
    }

    @GetMapping("")
    public String fetchReadingFromUrl(@RequestParam String url) {
        try {
            // Kết nối đến URL
            Document doc = Jsoup.connect(url).get();

            // Lấy phần tử chứa đoạn văn (theo class đã cho)
            Element readingDiv = doc.selectFirst("div.reading-text");

            if (readingDiv != null) {
                // Trả về nội dung HTML của đoạn văn
                return readingDiv.html();
            } else {
                return "Không tìm thấy nội dung phù hợp.";
            }
        } catch (IOException e) {
            return "Lỗi khi truy cập trang: " + e.getMessage();
        }
    }
}
