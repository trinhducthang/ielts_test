package com.trinhducthang.ielts_test.dto;

import com.trinhducthang.ielts_test.enums.Gender;
import com.trinhducthang.ielts_test.enums.Role;
import lombok.Data;


import java.time.LocalDate;
import java.util.Date;

@Data
public class UserDTO {
    private Integer id;

    private String username;

    private String password;

    private String email;

    private String fullName;

    private LocalDate dateOfBirth;

    private Gender gender;

    private Role role;

}
