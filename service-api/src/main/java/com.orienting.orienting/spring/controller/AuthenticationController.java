package com.orienting.orienting.spring.controller;

import com.orienting.common.dto.SignInDto;
import com.orienting.common.dto.SignInOrUpResponseDto;
import com.orienting.common.dto.UserCreationDto;
import com.orienting.common.entity.UserEntity;
import com.orienting.common.services.UserClubService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class AuthenticationController {
    private final UserClubService userClubService;
    private final ModelMapper modelMapper;

    @Autowired
    public AuthenticationController(UserClubService userClubService, ModelMapper modelMapper) {
        this.userClubService = userClubService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/signUp")
    public SignInOrUpResponseDto signUp(@RequestBody @Valid UserCreationDto user) throws Exception {
        userClubService.createUser(modelMapper.map(user, UserEntity.class));
        return new SignInOrUpResponseDto("success", "User created successfully!");
    }
}
