package com.codmain.orderapi.converters;

import com.codmain.orderapi.dtos.SingupRequestDTO;
import com.codmain.orderapi.dtos.UserDTO;
import com.codmain.orderapi.entity.User;

public class UserConverter extends AbstractConverter<User, UserDTO> {

    @Override
    public UserDTO fromEntity(User entity) {
        if (entity == null)
            return null;
        return UserDTO.builder().id(entity.getId()).username(entity.getUsername()).build();
    }

    @Override
    public User fromDTO(UserDTO dto) {
        if (dto == null)
            return null;
        return User.builder().id(dto.getId()).username(dto.getUsername()).build();
    }

    public User signup(SingupRequestDTO dto) {
        if (dto == null)
            return null;
        return User.builder().username(dto.getUsername()).password(dto.getPassword()).build();
    }

}
