package com.orienting.service.controller;

import com.orienting.common.dto.SignInDto;
import com.orienting.common.dto.AuthenticationResponseDto;
import com.orienting.common.dto.UserCreationDto;
import com.orienting.service.entity.UserEntity;
import com.orienting.service.services.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping(value = "/api/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthenticationController {
    private final AuthenticationService authService;
    private final ModelMapper modelMapper;

    @Autowired
    public AuthenticationController(AuthenticationService authService, ModelMapper modelMapper) {
        this.authService = authService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponseDto> register(@RequestBody @Valid UserCreationDto request) {
        return ResponseEntity.ok(authService.register(modelMapper.map(request, UserEntity.class)));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponseDto> signIn(@RequestBody SignInDto request) {
        return ResponseEntity.ok(authService.authenticate(modelMapper.map(request, UserEntity.class)));
    }

    @PostMapping("/refresh-token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        authService.refreshToken(request, response);
    }
}
