package com.englishtest.englishtest.mapper;

import com.englishtest.englishtest.dto.UserDTO;
import com.englishtest.englishtest.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserDTO toDto(User user){
        if ( user == null ) {
            return null;
        }

        UserDTO userDTO = new UserDTO();

        userDTO.setId( user.getId() );
        userDTO.setUsername( user.getUsername() );
        userDTO.setPassword( user.getPassword() );
        userDTO.setFullName( user.getFullName() );
        userDTO.setRole( user.getRole() );

        return userDTO;
    }

    public User toEntity(UserDTO userDTO){
        if ( userDTO == null ) {
            return null;
        }

        User.UserBuilder user = User.builder();

        user.id( userDTO.getId() );
        user.username( userDTO.getUsername() );
        user.password( userDTO.getPassword() );
        user.fullName( userDTO.getFullName() );
        user.role( userDTO.getRole() );

        return user.build();
    }
}
