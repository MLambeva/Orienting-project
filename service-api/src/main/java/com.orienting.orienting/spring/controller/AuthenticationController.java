package com.orienting.orienting.spring.controller;

import com.orienting.common.dto.SignInDto;
import com.orienting.common.dto.UserCreationDto;
import com.orienting.common.entity.AuthenticationTokenEntity;
import com.orienting.common.entity.UserEntity;
import com.orienting.common.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService service;
    private final ModelMapper modelMapper;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationTokenEntity> register (@RequestBody UserCreationDto request) {
        return ResponseEntity.ok(service.register(modelMapper.map(request, UserEntity.class)));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationTokenEntity> signIn(@RequestBody SignInDto request) {
        return ResponseEntity.ok(service.authenticate(modelMapper.map(request, UserEntity.class)));
    }
}
