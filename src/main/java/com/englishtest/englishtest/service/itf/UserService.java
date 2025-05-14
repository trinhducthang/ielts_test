package com.englishtest.englishtest.service.itf;

import com.englishtest.englishtest.dto.UserDTO;
import com.englishtest.englishtest.entity.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public UserDTO createUser(UserDTO user);

//    @PostAuthorize("(returnObject.username == authentication.name) || hasRole('ROLE_ADMIN')")
    public boolean deleteUser(Long id);

//    @PostAuthorize("(returnObject.username == authentication.name) || hasRole('ROLE_ADMIN')")
    public UserDTO getUser(Long id);

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_TEACHER')")
    public List<UserDTO> getUsers();

    public String getFullName(String username);

    public User getUserByUserName(String username);

    public User updatePassword(String username, String password);

}
