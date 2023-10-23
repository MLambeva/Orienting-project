package com.orienting.controller;

import com.orienting.common.dto.AuthenticationResponseDto;
import com.orienting.common.dto.SignInDto;
import com.orienting.common.dto.UserCreationDto;

public class AuthenticationController extends MainController {
    private final String AUTH_URL = "http://localhost:8080/api/auth";

    public AuthenticationResponseDto register(UserCreationDto input) {
        return (AuthenticationResponseDto) makeRequest(input, AUTH_URL + "/register", "POST",  new AuthenticationResponseDto());
    }

    public AuthenticationResponseDto authenticate(SignInDto input) {
        return (AuthenticationResponseDto) makeRequest(input, AUTH_URL + "/authenticate", "POST",  new AuthenticationResponseDto());
    }
}
