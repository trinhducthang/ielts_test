package com.englishtest.englishtest.entity.listening;

import com.englishtest.englishtest.entity.QuestionAnswer;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "listening_assignments")
public class ListeningAssignment {
    @Id
    private String id;

    private String description;
    private LocalDate timeCreated;
    private LocalDate dueDate;

    // Đường dẫn file PDF bài nghe (có thể là URL hoặc path trong storage)
    private String readingPdfUrl;

    // Đường dẫn file nghe MP3
    private String audioMp3Url;

    // List UserId được giao bài
    private List<Long> assignedUserIds = new ArrayList<>();

    // Key đáp án cho từng câu đọc (được load từ file Excel)
    private List<QuestionAnswer> answerKey = new ArrayList<>();

    // List submission của học sinh (embedded document)
    private List<ListeningSubmission> submissions = new ArrayList<>();
}
