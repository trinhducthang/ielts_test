package com.englishtest.englishtest.dto;

import com.englishtest.englishtest.entity.User;
import com.englishtest.englishtest.entity._enum.Role;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {

    private Long id;

    @NotNull(message = "username can be not null")
    private String username;

    private String password;

    private String fullName;

    private Role role;

}
