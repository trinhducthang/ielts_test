package com.trinhducthang.ielts_test.mapper;

import com.trinhducthang.ielts_test.dto.UserDTO;
import com.trinhducthang.ielts_test.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public User UserDTOToUser(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        user.setEmail(userDTO.getEmail());
        user.setFullName(userDTO.getFullName());
        user.setDateOfBirth(userDTO.getDateOfBirth());
        user.setGender(userDTO.getGender());
        return user;

    }
}
