package com.trinhducthang.ielts_test.dto;

import com.trinhducthang.ielts_test.enums.Gender;
import lombok.Data;


import java.util.Date;

@Data
public class UserDTO {
    private Integer id;

    private String username;

    private String password;

    private String email;

    private String fullName;

    private Date dateOfBirth;

    private Gender gender;

}
