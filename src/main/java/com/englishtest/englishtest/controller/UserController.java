package com.englishtest.englishtest.controller;

import com.englishtest.englishtest.dto.UserDTO;
import com.englishtest.englishtest.dto.response.ApiResponse;
import com.englishtest.englishtest.entity.User;
import com.englishtest.englishtest.repository.UserRepository;
import com.englishtest.englishtest.service.itf.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;

    @PostMapping("/create")
    public ApiResponse<UserDTO> createUser(@Valid @RequestBody UserDTO user){
        System.out.println("createUser called with: " + user);

        try {
            return ApiResponse.<UserDTO>builder()
                    .code(HttpStatus.OK.value())
                    .message("Create user success!")
                    .result(userService.createUser(user))
                    .build();
        }
        catch (Exception e) {
            return ApiResponse.<UserDTO>builder()
                    .code(HttpStatus.BAD_REQUEST.value())
                    .message(e.getMessage())
                    .result(null)
                    .build();
        }
    }


    @GetMapping("/info")
    public ApiResponse<UserDTO> getUser(@RequestParam long id){
        try {
            return ApiResponse.<UserDTO>builder()
                    .code(HttpStatus.OK.value())
                    .message("Get user success!")
                    .result(userService.getUser(id))
                    .build();
        }
        catch (Exception e) {
            return ApiResponse.<UserDTO>builder()
                    .code(HttpStatus.BAD_REQUEST.value())
                    .message(e.getMessage())
                    .build();
        }
    }


    @DeleteMapping("/info")
    public ApiResponse<?> deleteUser(@RequestParam long id){
        try{
            return new ApiResponse<>(HttpStatus.OK.value(), "Delete success",userService.deleteUser(id));
        }
        catch (Exception e) {
            return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "Delete failed because "+ e.getMessage(), null);
        }
    }



    @GetMapping("/all")
    public ApiResponse<List<UserDTO>> getAllUsers(){
        return ApiResponse.<List<UserDTO>>builder()
                .code(HttpStatus.OK.value())
                .message("Get all users success!")
                .result(userService.getUsers())
                .build();
    }




    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_TEACHER')")
    @GetMapping
    public Page<User> getUsers(@RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "5") int size) {
        return userRepository.findAll(PageRequest.of(page, size));
    }



    @GetMapping("/getname")
    public String getFullName(@RequestParam String userName) {
        return userService.getFullName(userName);
    }

    @GetMapping("/get-by-username")
    public User getByUserName(@RequestParam String username){
        return userService.getUserByUserName(username);
    }


    @PutMapping("/updatePassword/{username}")
    public ResponseEntity<String> updatePassword(@PathVariable String username, @RequestParam String password) {
        try{
            userService.updatePassword(username, password);
            return ResponseEntity.ok("Password updated successfully");
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
