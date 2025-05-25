package com.englishtest.englishtest.service;


import com.englishtest.englishtest.dto.UserDTO;
import com.englishtest.englishtest.entity.User;
import com.englishtest.englishtest.mapper.UserMapper;
import com.englishtest.englishtest.repository.UserRepository;
import com.englishtest.englishtest.service.itf.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    @Override
    public UserDTO createUser(UserDTO user) {
        if (checkOverlap(userMapper.toEntity(user))) {
            throw new RuntimeException("username already exists");
        }
        User createUser = userMapper.toEntity(user);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        createUser.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setPassword("**************");
        userRepository.save(createUser);
        return user;
    }



    @Override
    public String getFullName(String username) {
        User user = userRepository.getUserByUsername(username);
        return user.getFullName();
    }

    @Override
    public User getUserByUserName(String username) {
        return userRepository.getUserByUsername(username);
    }


    @Transactional
    @Override
    public User updatePassword(String username, String password) {
        User user = userRepository.getUserByUsername(username);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        user.setPassword(passwordEncoder.encode(password));
        return userRepository.save(user);
    }

    @Override
    public boolean deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(()-> new RuntimeException("user not exits"));
        String authenticationName = SecurityContextHolder.getContext().getAuthentication().getName();
        String username = user.getUsername();
        if(!authenticationName.equals(username) && !authenticationName.equals("admin@")) throw new RuntimeException("Invalid authentication");
        userRepository.delete(user);
        return true;
    }

    @Override
    public UserDTO getUser(Long id) {
        UserDTO userDTO = userMapper.toDto(userRepository.findById(id).orElseThrow(()-> new RuntimeException("user not exits")));
        userDTO.setPassword(null);
        return userDTO;
    }

    @Override
    public List<UserDTO> getUsers() {
        List<User> users = userRepository.findAll();
        List<UserDTO> userDTOs = new ArrayList<>();
        for (User user : users) {
            userDTOs.add(userMapper.toDto(user));
        }
        return userDTOs;
    }






    public boolean checkOverlap(User user) {
        return userRepository.findByUsername(user.getUsername()) != null;
    }



}
