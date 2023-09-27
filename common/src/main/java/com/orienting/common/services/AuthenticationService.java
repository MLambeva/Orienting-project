package com.orienting.common.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orienting.common.dto.AuthenticationResponseDto;
import com.orienting.common.entity.TokenEntity;
import com.orienting.common.entity.UserEntity;
import com.orienting.common.repository.TokenRepository;
import com.orienting.common.repository.UserRepository;
import com.orienting.common.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@ComponentScan(basePackages = "com.orienting.common.utils")
public class AuthenticationService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    private void saveUserToken(UserEntity user, String jwtToken) {
        TokenEntity token = TokenEntity.builder()
                .user(user)
                .token(jwtToken)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }
    public AuthenticationResponseDto register(UserEntity request) {
        UserEntity user = UserEntity.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .ucn(request.getUcn())
                .phoneNumber(request.getPhoneNumber())
                .group(request.getGroup())
                .role(request.getRole())
                .club(request.getClub())
                .build();
        UserEntity savedUser = userRepository.save(user);
        String jwtToken = jwtUtils.generateToken(user);
        System.out.println(jwtToken);
        String refreshToken = jwtUtils.generateRefreshToken(user);
        saveUserToken(savedUser, jwtToken);
        return AuthenticationResponseDto.builder().accessToken(jwtToken).refreshToken(refreshToken).build();
    }

    public AuthenticationResponseDto authenticate(UserEntity request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        UserEntity user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        String jwtToken = jwtUtils.generateToken(user);
        String refreshToken = jwtUtils.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponseDto.builder().accessToken(jwtToken).refreshToken(refreshToken).build();
    }

    private void revokeAllUserTokens(UserEntity user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getUserId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            return;
        }
        final String refreshToken = authHeader.substring(7);
        final String userEmail = jwtUtils.extractEmail(refreshToken);
        if (userEmail != null) {
            UserEntity user = this.userRepository.findByEmail(userEmail)
                    .orElseThrow();
            if (jwtUtils.isTokenValid(refreshToken, user)) {
                String accessToken = jwtUtils.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                AuthenticationResponseDto authResponse = AuthenticationResponseDto.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }
}
