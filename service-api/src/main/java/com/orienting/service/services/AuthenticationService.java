package com.orienting.service.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orienting.service.dto.AuthenticationResponseDto;
import com.orienting.service.entity.ClubEntity;
import com.orienting.service.entity.TokenEntity;
import com.orienting.service.entity.UserEntity;
import com.orienting.service.exception.InvalidInputException;
import com.orienting.service.exception.NoExistedClubException;
import com.orienting.service.exception.NoExistedUserException;
import com.orienting.service.repository.ClubRepository;
import com.orienting.service.repository.TokenRepository;
import com.orienting.service.repository.UserRepository;
import com.orienting.service.utils.JwtUtils;
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
@ComponentScan(basePackages = "com.orienting.service.utils")
public class AuthenticationService {
    private final UserRepository userRepository;
    private final ClubRepository clubRepository;
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

    public AuthenticationResponseDto register(UserEntity request) {
        if (request == null) {
            throw new InvalidInputException("Input user is null!");
        }
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new InvalidInputException(String.format("User with email %s already exist!", request.getEmail()));
        }
        ClubEntity newClub = null;
        if(request.getClub() != null) {
            if(request.getClub().getClubId() != null) {
                newClub = clubRepository.findClubByClubId(request.getClub().getClubId()).orElseThrow(() -> new NoExistedClubException(String.format("Club with id %d does not exist!", request.getClub().getClubId())));
            }
            else if(request.getClub().getClubName() != null){
                newClub = clubRepository.findClubByClubName(request.getClub().getClubName()).orElseThrow(() -> new NoExistedClubException(String.format("Club with name %s does not exist!", request.getClub().getClubName())));
            }
        }
        UserEntity user = UserEntity.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .ucn(request.getUcn())
                .phoneNumber(request.getPhoneNumber())
                .group(request.getGroup())
                .role(request.getRole())
                .club(newClub)
                .build();
        UserEntity savedUser = userRepository.save(user);
        String jwtToken = jwtUtils.generateToken(user);
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
        UserEntity user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new NoExistedUserException(String.format("User with email %s does not exist!", request.getEmail())));
        String jwtToken = jwtUtils.generateToken(user);
        String refreshToken = jwtUtils.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponseDto.builder().accessToken(jwtToken).refreshToken(refreshToken).build();
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
                    .orElseThrow(() -> new NoExistedUserException(String.format("User with email %s does not exist!", userEmail)));
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
