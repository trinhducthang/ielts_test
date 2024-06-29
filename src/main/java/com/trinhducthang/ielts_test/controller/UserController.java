package com.trinhducthang.ielts_test.controller;

import com.trinhducthang.ielts_test.dto.UserDTO;
import com.trinhducthang.ielts_test.model.User;
import com.trinhducthang.ielts_test.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/insert")
    public User insert(@RequestBody UserDTO userDTO) {
        return userService.insertUser(userDTO);
    }

    @PostMapping("/update/{id}")
    public User update(@PathVariable long id, @RequestBody UserDTO userDTO) {
        return userService.updateUser(userDTO,id);
    }

    @GetMapping()
    public List<User> getAll() {
        return userService.getUsers();
    }

}
