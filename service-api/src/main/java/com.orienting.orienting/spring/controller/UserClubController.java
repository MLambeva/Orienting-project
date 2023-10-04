package com.orienting.orienting.spring.controller;

import com.orienting.common.dto.UserDto;
import com.orienting.common.dto.UserUpdateDto;
import com.orienting.common.entity.UserEntity;
import com.orienting.common.services.UserClubService;
import jakarta.validation.Valid;
import lombok.Getter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/users", produces = MediaType.APPLICATION_JSON_VALUE)
@Getter
public class UserClubController {
    private final UserClubService userClubService;
    private final ModelMapper modelMapper;

    @Autowired
    public UserClubController(UserClubService userClubService, ModelMapper modelMapper) {
        this.userClubService = userClubService;
        this.modelMapper = modelMapper;
    }

    @PutMapping("setCoach/{userId}/{clubId}")
    public ResponseEntity<UserDto> setCoachToClub(@PathVariable("userId") Integer userId, @PathVariable("clubId") Integer clubId) {
        return ResponseEntity.ok(modelMapper.map(userClubService.setCoachToClub(userId, clubId), UserDto.class));
    }

    @PutMapping("/addClub/{userId}/{clubId}")
    public ResponseEntity<UserDto> addClubToUser(@PathVariable("userId") Integer userId, @PathVariable("clubId") Integer clubId) {
        return ResponseEntity.ok(modelMapper.map(userClubService.addClubToUser(userId, clubId), UserDto.class));
    }

    @PutMapping("/addClub/{clubId}")
    public ResponseEntity<UserDto> addClub(@PathVariable("clubId") Integer clubId, Authentication authentication) {
        return addClubToUser(userClubService.findAuthenticatedUser(authentication.getName()).getUserId(), clubId);
    }

    @PutMapping("/update/byUserId/{userId}")//for admins & coaches
    public ResponseEntity<UserDto> updateUserByUserId(@PathVariable("userId") Integer userId, @Valid @RequestBody UserUpdateDto newUser, Authentication authentication) {
        UserDto user = modelMapper.map(userClubService.updateUserByUserId(userId, authentication.getName(), modelMapper.map(newUser, UserEntity.class)), UserDto.class);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/update/byUcn/{ucn}")//for admins & coaches
    public ResponseEntity<UserDto> updateUserByUcn(@PathVariable("ucn") String ucn, @Valid @RequestBody UserUpdateDto newUser, Authentication authentication) {
        UserDto user = modelMapper.map(userClubService.updateUserByUcn(ucn, authentication.getName(), modelMapper.map(newUser, UserEntity.class)), UserDto.class);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/update") // for all
    public ResponseEntity<UserDto> update(@Valid @RequestBody UserUpdateDto newUser, Authentication authentication) {
        UserDto user = modelMapper.map(userClubService.update(authentication.getName(), modelMapper.map(newUser, UserEntity.class)), UserDto.class);
        return ResponseEntity.ok(user);
    }
}
