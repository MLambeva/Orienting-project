package com.orienting.orienting.spring.controller;

import com.orienting.common.dto.UserDto;
import com.orienting.common.dto.UserUpdateDto;
import com.orienting.common.entity.UserEntity;
import com.orienting.common.services.UserService;
import jakarta.validation.Valid;
import lombok.Getter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/byUserId/{userId}")
    public ResponseEntity<UserDto> getUserByUserId(@PathVariable("userId") Integer userId) {
        return ResponseEntity.ok(modelMapper.map(userService.getUserById(userId), UserDto.class));
    }

    @GetMapping("/byUcn/{ucn}")
    public ResponseEntity<UserDto> getUserByUserId(@PathVariable("ucn") String ucn) {
        return ResponseEntity.ok(modelMapper.map(userService.getUserByUcn(ucn), UserDto.class));
    }

    @GetMapping("/byClub/{clubId}")
    public ResponseEntity<List<UserDto>> getUsersByClubId(@PathVariable("clubId") Integer clubId) {
        return ResponseEntity.ok(userService.getAllUsersByClubId(clubId).stream()
                .map(user -> modelMapper.map(user, UserDto.class)).toList());
    }

    public ResponseEntity<List<UserDto>> getUsersByClubIdHelper(Integer clubId, String role) {
        return ResponseEntity.ok(userService.getAllCoachesByClubId(clubId, role).stream()
                .map(club -> modelMapper.map(club, UserDto.class))
                .toList());
    }

    @GetMapping("/allCoaches/{clubId}")
    public ResponseEntity<List<UserDto>> getCoachesByClubId(@PathVariable("clubId") Integer clubId) {
        return getUsersByClubIdHelper(clubId, "coach");
    }

    @GetMapping("/allCompetitors/{clubId}")
    public ResponseEntity<List<UserDto>> getCompetitorsByClubId(@PathVariable("clubId") Integer clubId) {
        return getUsersByClubIdHelper(clubId, "competitor");
    }

    @GetMapping("/roleByUserId/{userId}")
    public ResponseEntity<String> getRoleByUserId(@PathVariable("userId") Integer userId) {
        return ResponseEntity.ok(userService.getRoleByUserId(userId));
    }

    @GetMapping("/roleByUcn/{ucn}")
    public ResponseEntity<String> getRoleByUcn(@PathVariable("ucn") String ucn) {
        return ResponseEntity.ok(userService.getRoleByUcn(ucn));
    }

    public ResponseEntity<String> deleteUserBy(UserDto user, String identifier, String identifierType) {
        if (user != null) {
            return ResponseEntity.ok(String.format("User with %s: %s was deleted!", identifierType, identifier));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/byUserId/{userId}")
    public ResponseEntity<String> deleteUserByUserId(@PathVariable("userId") Integer userId) {
        return deleteUserBy(modelMapper.map(userService.deleteUserByUserId(userId, false), UserDto.class), userId.toString(), "userId");
    }

    //for Admins
    @DeleteMapping("/byUserIdAdmin/{userId}")
    public ResponseEntity<String> deleteUserByUserIdAdmin(@PathVariable("userId") Integer userId) {
        return deleteUserBy(modelMapper.map(userService.deleteUserByUserId(userId, true), UserDto.class), userId.toString(), "userId");
    }

    @DeleteMapping("/byUcn/{ucn}")
    public ResponseEntity<String> deleteUserByUcn(@PathVariable("ucn") String ucn) {
        return deleteUserBy(modelMapper.map(userService.deleteUserByUcn(ucn, false), UserDto.class), ucn, "ucn");
    }

    //for Admins
    @DeleteMapping("/byUcnAdmin/{ucn}")
    public ResponseEntity<String> deleteUserByUcnAdmin(@PathVariable("ucn") String ucn) {
        return deleteUserBy(modelMapper.map(userService.deleteUserByUcn(ucn, true), UserDto.class), ucn, "ucn");
    }

    public ResponseEntity<String> updateUserBy(UserUpdateDto user, String identifier, String identifierType) {
        if (user != null) {
            return ResponseEntity.ok(String.format("User with %s: %s was updated!", identifierType, identifier));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/byUserId/{userId}")
    public ResponseEntity<String> updateUserByUserId(@PathVariable("userId") Integer userId, @Valid @RequestBody UserUpdateDto newUser) {
        UserEntity newUserEntity = modelMapper.map(newUser, UserEntity.class);
        UserUpdateDto user = modelMapper.map(userService.updateUserByUserId(userId, false, newUserEntity), UserUpdateDto.class);
        return updateUserBy(user, userId.toString(), "userId");
    }

    @PutMapping("/byUserIdAdmin/{userId}")
    public ResponseEntity<String> updateUserByUserIdAdmin(@PathVariable("userId") Integer userId, @Valid @RequestBody UserUpdateDto newUser) {
        UserEntity newUserEntity = modelMapper.map(newUser, UserEntity.class);
        UserUpdateDto user = modelMapper.map(userService.updateUserByUserId(userId, true, newUserEntity), UserUpdateDto.class);
        return updateUserBy(user, userId.toString(), "userId");
    }

    @PutMapping("byUcn/{ucn}")
    public ResponseEntity<String> updateUserByUcn(@PathVariable("ucn") String ucn, @Valid @RequestBody UserUpdateDto newUser) {
        UserEntity newUserEntity = modelMapper.map(newUser, UserEntity.class);
        UserUpdateDto user = modelMapper.map(userService.updateUserByUcn(ucn, false, newUserEntity), UserUpdateDto.class);
        return updateUserBy(user, ucn, "ucn");
    }

    @PutMapping("/byUcnAdmin/{ucn}")
    public ResponseEntity<String> updateUserByUcnAdmin(@PathVariable("ucn") String ucn, @Valid @RequestBody UserUpdateDto newUser) {
        UserEntity newUserEntity = modelMapper.map(newUser, UserEntity.class);
        UserUpdateDto user = modelMapper.map(userService.updateUserByUcn(ucn, true, newUserEntity), UserUpdateDto.class);
        return updateUserBy(user, ucn, "ucn");
    }
}
