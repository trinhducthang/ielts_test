package com.englishtest.englishtest.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table
public class Course {
    @Id @GeneratedValue()
    private Long id;
    private String name;

    @OneToMany(mappedBy = "course")
    @JsonManagedReference
    private List<User> users;

}
