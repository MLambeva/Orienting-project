package com.orienting.orienting.spring.controller;

import com.orienting.common.dto.CoachCreationDto;
import com.orienting.common.dto.UserCreationDto;
import com.orienting.common.entity.UserEntity;
import com.orienting.common.services.UserClubService;
import jakarta.validation.Valid;
import lombok.Getter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.sql.In;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Getter
public class UserClubController {
    private final UserClubService userClubService;
    private final ModelMapper modelMapper;

    @Autowired
    public UserClubController(UserClubService userClubService, ModelMapper modelMapper) {
        this.userClubService = userClubService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/users/add")
    public ResponseEntity<String> addUser(@RequestBody @Valid UserCreationDto userDto) {
        UserEntity user = userClubService.createUser(modelMapper.map(userDto, UserEntity.class));
        return ResponseEntity.ok(String.format("User with id: %d was added!", user.getUserId()));
    }

    @PostMapping("/addCoach/{clubId}")
    public ResponseEntity<String> addCoach(@PathVariable("clubId") Integer clubId, @Valid @RequestBody CoachCreationDto coach) {
        UserEntity user = userClubService.addCoach(clubId, modelMapper.map(coach, UserEntity.class));
        return ResponseEntity.ok(String.format("Club with id %d has new coach with id %d!", clubId, user.getUserId()));
    }

    @PutMapping("/{userId}/{clubId}")
    public ResponseEntity<String> makeOrChangeCoach(@PathVariable("userId") Integer userId, @PathVariable("clubId") Integer clubId) {
        userClubService.makeAndChangeCoach(userId, clubId);
        return ResponseEntity.ok(String.format("User with id %d belong to club with id %d!", userId, clubId));
    }

    @PutMapping("makeCoach/{userId}")
    public ResponseEntity<String> makeCoach(@PathVariable("userId") Integer userId) {
        userClubService.makeCoach(userId);
        return ResponseEntity.ok(String.format("User with id %d is coach!", userId));
    }

    @PutMapping("/add/{userId}/{clubId}")
    public ResponseEntity<String> addClubToUser(@PathVariable("userId") Integer userId, @PathVariable("clubId") Integer clubId) {
        userClubService.addClubToUser(userId, clubId);
        return ResponseEntity.ok(String.format("User with id %d belong to club with id %d!", userId, clubId));
    }

}
