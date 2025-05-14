package com.englishtest.englishtest.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Question {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String questionText;
    @Enumerated(EnumType.STRING)
    private QuestionType questionType;
    @ElementCollection
    private List<String> choices = new ArrayList<>();


    public enum QuestionType { MULTIPLE_CHOICE, TRUE_FALSE_NOT_GIVEN, MATCHING }

}
