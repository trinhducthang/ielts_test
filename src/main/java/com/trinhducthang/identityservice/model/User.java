package com.trinhducthang.identityservice.model;

import com.trinhducthang.identityservice.enums.Gender;
import com.trinhducthang.identityservice.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String username;

    private String password;

    private String email;

    private String fullName;

    private LocalDate dateOfBirth;

    private Gender gender;

    private Role role;


    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDate createdAt;


    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDate updatedAt;



}
