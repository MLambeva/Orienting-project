package com.orienting.controller;

import com.orienting.common.dto.AuthenticationResponseDto;
import com.orienting.common.dto.SignInDto;
import com.orienting.common.dto.UserCreationDto;

public class AuthenticationController extends MainController {

    public AuthenticationController(String authUrl) {
        super(authUrl);
    }

    public AuthenticationResponseDto register(UserCreationDto input) {
        return (AuthenticationResponseDto) makeRequest(input, url + "/register", "POST",  new AuthenticationResponseDto());
    }

    public AuthenticationResponseDto authenticate(SignInDto input) {
        return (AuthenticationResponseDto) makeRequest(input, url + "/authenticate", "POST",  new AuthenticationResponseDto());
    }
}
