package com.trinhducthang.ielts_test.service.impl;

import com.trinhducthang.ielts_test.dto.UserDTO;
import com.trinhducthang.ielts_test.enums.Role;
import com.trinhducthang.ielts_test.mapper.UserMapper;
import com.trinhducthang.ielts_test.model.User;
import com.trinhducthang.ielts_test.repository.UserRepository;
import com.trinhducthang.ielts_test.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Override
    public User insertUser(UserDTO userDTO) {
        User user = userMapper.UserDTOToUser(userDTO);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);
        return userRepository.save(user);
    }

    @Override
    public User updateUser(UserDTO userDTO, long id) {
        User user = userRepository.getById(id);
        if (user != null) {
            user = userMapper.UserDTOToUser(userDTO);
        }
        return userRepository.save(user);
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }
}
