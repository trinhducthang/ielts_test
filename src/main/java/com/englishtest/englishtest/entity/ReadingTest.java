package com.englishtest.englishtest.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;

import java.util.List;

@Entity
@Data
@Table
public class ReadingTest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String url;

    @Lob
    private String content;

    private String title;

    private String urlImage;

    @ManyToMany(mappedBy = "readingTests")
    @JsonIgnore
    private List<Course> courses;


    public ReadingTest(String url, String html) {
        this.url = url;
        this.content = html;
    }

    public ReadingTest() {

    }
}

