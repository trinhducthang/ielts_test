package com.englishtest.englishtest.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
public class ReadingTest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String url;

    @Lob
    private String content;

    public ReadingTest(String url, String html) {
        this.url = url;
        this.content = html;
    }

    public ReadingTest() {

    }
}

