package com.englishtest.englishtest.entity;

import lombok.Data;

@Data
public class QuestionAnswer {
    private int questionNumber;  // ví dụ: 1, 2, 3
    private String correctAnswer;
}
