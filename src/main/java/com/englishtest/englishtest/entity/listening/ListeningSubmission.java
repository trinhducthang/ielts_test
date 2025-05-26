package com.englishtest.englishtest.entity.listening;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
public class ListeningSubmission {
    private Long userId;
    private Map<Integer, String> answers; // key = questionNumber, value = student's answer
    private LocalDateTime submittedAt;

    private int score; // optional
    private boolean submitted;
}
