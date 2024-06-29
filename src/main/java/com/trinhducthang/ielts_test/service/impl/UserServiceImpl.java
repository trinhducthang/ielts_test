package com.trinhducthang.ielts_test.service.impl;

import com.trinhducthang.ielts_test.dto.UserDTO;
import com.trinhducthang.ielts_test.mapper.UserMapper;
import com.trinhducthang.ielts_test.model.User;
import com.trinhducthang.ielts_test.repository.UserRepository;
import com.trinhducthang.ielts_test.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Override
    public User insertUser(UserDTO userDTO) {
        User user = userMapper.UserDTOToUser(userDTO);
        return userRepository.save(user);
    }
}
