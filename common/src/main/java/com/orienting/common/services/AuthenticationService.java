package com.orienting.common.services;

import com.orienting.common.entity.AuthenticationTokenEntity;
import com.orienting.common.entity.UserEntity;
import com.orienting.common.exception.InvalidInputException;
import com.orienting.common.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final TokenRepository tokenRepository;

    @Autowired
    public AuthenticationService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }
    public void saveConfirmationToken(AuthenticationTokenEntity authenticationToken) {
        tokenRepository.save(authenticationToken);
    }

    public AuthenticationTokenEntity getToken(UserEntity user) {
        return tokenRepository.findTokenByUser(user).orElseThrow(() -> new InvalidInputException(""));
    }
}
