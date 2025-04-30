package com.englishtest.englishtest.controller;

import com.englishtest.englishtest.entity.Passage;
import com.englishtest.englishtest.entity.Question;
import com.englishtest.englishtest.entity.ReadingTest;
import com.englishtest.englishtest.repository.PassageRepository;
import com.englishtest.englishtest.repository.QuestionRepository;
import com.englishtest.englishtest.repository.ReadingTestRepository;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/teacher")
public class TeacherRestController {

    private final ReadingTestRepository readingTestRepo;
    private final PassageRepository passageRepo;
    private final QuestionRepository questionRepo;

    public TeacherRestController(ReadingTestRepository readingTestRepo, PassageRepository passageRepo, QuestionRepository questionRepo) {
        this.readingTestRepo = readingTestRepo;
        this.passageRepo = passageRepo;
        this.questionRepo = questionRepo;
    }

}