package com.englishtest.englishtest.entity.writing;


import lombok.Data;

import java.util.List;

@Data
public class Feedback {
    private String comment;
    private List<InlineComment> inlineComments; // 👈 Add this

    public boolean isFeedbackGiven() {
        return comment != null && !comment.trim().isEmpty();
    }

    @Data
    public static class InlineComment {
        private String text;      // selected text
        private String comment;   // comment for the selected text
    }
}

