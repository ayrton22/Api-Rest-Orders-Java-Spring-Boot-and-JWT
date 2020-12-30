package com.codmain.orderapi.controllers;

import com.codmain.orderapi.converters.UserConverter;
import com.codmain.orderapi.dtos.LoginRequestDTO;
import com.codmain.orderapi.dtos.LoginResponseDTO;
import com.codmain.orderapi.dtos.SingupRequestDTO;
import com.codmain.orderapi.dtos.UserDTO;
import com.codmain.orderapi.entity.User;
import com.codmain.orderapi.services.UserService;
import com.codmain.orderapi.utils.WrapperResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserConverter userConverter;

    @PostMapping(value = "/signup")
    public ResponseEntity<WrapperResponse<UserDTO>> signup(@RequestBody SingupRequestDTO request) {
        User user = userService.createUser(userConverter.signup(request));
        return new WrapperResponse<>(true, "success", userConverter.fromEntity(user)).createResponse();
    }

    @PostMapping(value = "/login")
    public ResponseEntity<WrapperResponse<LoginResponseDTO>> login(@RequestBody LoginRequestDTO request) {
        LoginResponseDTO response = userService.login(request);

        return new WrapperResponse<>(true, "success", response).createResponse();
    }
}
