package com.englishtest.englishtest.entity.writing;


import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Document("writing_assignments")
public class WritingAssignment {
    @Id
    private String id;

    private String description;
    private LocalDate timeCreated;
    private LocalDate dueDate;
    private String imageUrl;

    private List<Long> assignedUserIds;

    private List<Submission> submissions = new ArrayList<>();
}

