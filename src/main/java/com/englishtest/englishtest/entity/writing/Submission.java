package com.englishtest.englishtest.entity.writing;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Submission {
    private Long userId;
    private String content;
    private LocalDateTime submittedAt;
    private Feedback feedback;

    private boolean submitted;

    @JsonProperty("feedbackGiven")
    public boolean isFeedbackGiven() {
        return feedback != null && feedback.getComment() != null && !feedback.getComment().trim().isEmpty();
    }
}

