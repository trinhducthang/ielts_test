package com.trinhducthang.ielts_test.service;

import com.trinhducthang.ielts_test.dto.UserDTO;
import com.trinhducthang.ielts_test.model.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    public User insertUser(UserDTO userDTO);
}
