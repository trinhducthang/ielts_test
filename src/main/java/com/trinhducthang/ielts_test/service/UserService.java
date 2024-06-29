package com.trinhducthang.ielts_test.service;

import com.trinhducthang.ielts_test.dto.UserDTO;
import com.trinhducthang.ielts_test.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    public User insertUser(UserDTO userDTO);

    public User updateUser(UserDTO userDTO, long id);

    public List<User> getUsers ();
}
