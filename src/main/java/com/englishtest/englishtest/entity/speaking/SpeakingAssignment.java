package com.englishtest.englishtest.entity.speaking;


import com.englishtest.englishtest.entity.writing.Submission;
import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Document("speaking_assignments")
public class SpeakingAssignment {
    @Id
    private String id;

    private String description;
    private LocalDate timeCreated;
    private LocalDate dueDate;

    private List<Long> assignedUserIds;

    private List<Submission> submissions = new ArrayList<>();
}

