package com.englishtest.englishtest.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Entity
@Data
@Table
public class StudentAnswer {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long studentId;


    private String answer;

    private int points;

    private String totalTime;

    private LocalDateTime timeToSubmit;
}
