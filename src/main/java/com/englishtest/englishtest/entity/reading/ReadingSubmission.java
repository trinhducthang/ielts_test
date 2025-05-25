package com.englishtest.englishtest.entity.reading;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
public class ReadingSubmission {
    private Long userId;
    private Map<Integer, String> answers; // key = questionNumber, value = student's answer
    private LocalDateTime submittedAt;

    private int score; // optional
    private boolean submitted;
}
