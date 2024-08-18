package com.trinhducthang.identityservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.trinhducthang.identityservice.enums.Gender;
import com.trinhducthang.identityservice.enums.Role;
import lombok.Data;


import java.time.LocalDate;

@JsonInclude(JsonInclude.Include.NON_NULL)
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
