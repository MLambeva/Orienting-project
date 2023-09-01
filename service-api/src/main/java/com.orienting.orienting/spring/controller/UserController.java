package com.orienting.orienting.spring.controller;
import com.orienting.common.dto.UserDto;
import com.orienting.common.entity.UserEntity;
import com.orienting.common.services.UserService;
import jakarta.validation.Valid;
import lombok.Getter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/users")
@Getter
public class UserController {

    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    public UserController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }
    @GetMapping("/all")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> userDto = userService.getUsers().stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .collect(Collectors.toList());

        return ResponseEntity.ok(userDto);
    }
    @PostMapping("/user")
    public ResponseEntity<String> createUser(@RequestBody @Valid UserDto userDto) {
        UserEntity user = modelMapper.map(userDto, UserEntity.class);
        userService.createUser(user);
        return ResponseEntity.ok("User added!");
    }

    public ResponseEntity<String> deleteUserBy(UserDto user, String identifier, String identifierType) {
        if(user != null) {
            return ResponseEntity.ok(String.format("User with %s: %s was deleted!", identifierType, identifier));
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }
    @DeleteMapping("/byUserId/{userId}")
    public ResponseEntity<String> deleteUserByUserId(@PathVariable("userId") Integer userId) {
        UserDto userDto = modelMapper.map(userService.deleteUserByUserId(userId), UserDto.class);
        return deleteUserBy(userDto, userId.toString(), "userId");
    }
    @DeleteMapping("/byUcn/{ucn}")
    public ResponseEntity<String> deleteUserByUcn(@PathVariable("ucn") String ucn) {
        UserDto userDto = modelMapper.map(userService.deleteUserByUcn(ucn), UserDto.class);
        return  deleteUserBy(userDto, ucn, "ucn");
    }
}
