package com.orienting.service.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.orienting.common.dto.CompetitorDto;
import com.orienting.common.dto.CompetitorsWithCoachesDto;
import com.orienting.common.dto.UserDto;
import com.orienting.common.dto.UsersWithRequestedCompetitionsDto;
import com.orienting.service.entity.UserEntity;
import com.orienting.service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfiguration {
    private final UserRepository userRepository;
    @Bean
    public UserDetailsService userDetailsService() {
        return email -> userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found!"));
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.createTypeMap(UserEntity.class, UserDto.class)
                .addMapping(src -> src.getClub().getCity(), UserDto::setCity);
        modelMapper.createTypeMap(UserEntity.class, CompetitorDto.class)
                .addMapping(src -> src.getClub().getCity(), CompetitorDto::setCity);
        modelMapper.createTypeMap(UserEntity.class, CompetitorsWithCoachesDto.class)
                .addMapping(src -> src.getClub().getCity(), CompetitorsWithCoachesDto::setCity);
        modelMapper.createTypeMap(UserEntity.class, UsersWithRequestedCompetitionsDto.class)
                .addMapping(src -> src.getClub().getCity(), UsersWithRequestedCompetitionsDto::setCity);

        return modelMapper;
    }
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();

        objectMapper.registerModule(new JavaTimeModule());

        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.setDateFormat(new SimpleDateFormat("dd-MM-yyyy"));
        objectMapper.setTimeZone(TimeZone.getTimeZone("UTC"));

        return objectMapper;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
