package com.orienting.orienting.spring.controller;

import com.orienting.common.dto.UserCreationDto;
import com.orienting.common.dto.UserDto;
import com.orienting.common.entity.UserEntity;
import com.orienting.common.services.UserClubService;
import jakarta.validation.Valid;
import lombok.Getter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/api/users")
@Getter
public class UserClubController {
    private final UserClubService userClubService;
    private final ModelMapper modelMapper;

    @Autowired
    public UserClubController(UserClubService userClubService, ModelMapper modelMapper) {
        this.userClubService = userClubService;
        this.modelMapper = modelMapper;
    }

    //api/users/byId/{userId}
    @PutMapping("setCoach/{userId}/{clubId}")
    public ResponseEntity<UserDto> setCoachToClub(@PathVariable("userId") Integer userId, @PathVariable("clubId") Integer clubId) {
        return ResponseEntity.ok(modelMapper.map(userClubService.setCoachToClub(userId, clubId), UserDto.class));
    }

    @PutMapping("/add/{userId}/{clubId}")
    public ResponseEntity<UserDto> addClubToUser(@PathVariable("userId") Integer userId, @PathVariable("clubId") Integer clubId) {
        return ResponseEntity.ok(modelMapper.map(userClubService.addClubToUser(userId, clubId), UserDto.class));
    }

    @PostMapping("/addUser")
    public ResponseEntity<UserEntity> addUser(@RequestBody @Valid UserCreationDto userDto) throws URISyntaxException, Exception {
        UserEntity user = userClubService.createUser(modelMapper.map(userDto, UserEntity.class));
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(user.getUserId())
                .toUri();
        return ResponseEntity.created(uri)
                .body(user);
    }

}
