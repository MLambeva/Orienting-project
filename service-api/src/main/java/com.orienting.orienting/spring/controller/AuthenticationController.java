package com.orienting.orienting.spring.controller;

import com.orienting.common.dto.SignInDto;
import com.orienting.common.dto.UserCreationDto;
import com.orienting.common.entity.AuthenticationResponseEntity;
import com.orienting.common.entity.UserEntity;
import com.orienting.common.services.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authService;
    private final ModelMapper modelMapper;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponseEntity> register (@RequestBody @Valid UserCreationDto request) {
        return ResponseEntity.ok(authService.register(modelMapper.map(request, UserEntity.class)));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponseEntity> signIn(@RequestBody SignInDto request) {
        return ResponseEntity.ok(authService.authenticate(modelMapper.map(request, UserEntity.class)));
    }

    @PostMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        authService.refreshToken(request, response);
    }
}
