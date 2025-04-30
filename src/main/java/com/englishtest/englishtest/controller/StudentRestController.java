package com.englishtest.englishtest.controller;

import com.englishtest.englishtest.entity.Question;
import com.englishtest.englishtest.entity.StudentAnswer;
import com.englishtest.englishtest.repository.QuestionRepository;
import com.englishtest.englishtest.repository.StudentAnswerRepository;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/student")

public class StudentRestController {

    private final QuestionRepository questionRepo;
    private final StudentAnswerRepository answerRepo;

    public StudentRestController(QuestionRepository questionRepo, StudentAnswerRepository answerRepo) {
        this.questionRepo = questionRepo;
        this.answerRepo = answerRepo;
    }


}

