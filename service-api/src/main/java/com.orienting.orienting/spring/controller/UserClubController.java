package com.orienting.orienting.spring.controller;

import com.orienting.common.dto.UserCreationDto;
import com.orienting.common.entity.UserEntity;
import com.orienting.common.services.UserClubService;
import jakarta.validation.Valid;
import lombok.Getter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


}
