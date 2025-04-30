package com.englishtest.englishtest.dto;

public class QuestionDTO {
    private String id;
    private String text;

    // Constructors
    public QuestionDTO() {}

    public QuestionDTO(String id, String text) {
        this.id = id;
        this.text = text;
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
}
