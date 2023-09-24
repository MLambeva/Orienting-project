package com.orienting.common.services;

import com.orienting.common.entity.AuthenticationTokenEntity;
import com.orienting.common.entity.UserEntity;
import com.orienting.common.repository.UserRepository;
import com.orienting.common.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtService;
    private final AuthenticationManager authenticationManager;
    public AuthenticationTokenEntity register(UserEntity request) {
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
        userRepository.save(user);
        String jwtToken = jwtService.generateToken(user);
        return AuthenticationTokenEntity.builder().token(jwtToken).build();
    }

    public AuthenticationTokenEntity authenticate(UserEntity request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        UserEntity user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        String jwtToken = jwtService.generateToken(user);
        return AuthenticationTokenEntity.builder().token(jwtToken).build();
    }

}
