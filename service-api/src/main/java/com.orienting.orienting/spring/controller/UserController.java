package com.orienting.orienting.spring.controller;

import com.orienting.common.dto.*;
import com.orienting.common.entity.UserEntity;
import com.orienting.common.services.UserService;
import jakarta.validation.Valid;
import lombok.Getter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Set;
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
                .toList();
        return ResponseEntity.ok(userDto);
    }

    @GetMapping("/byId/{userId}")
    public ResponseEntity<UserDto> getUserByUserId(@PathVariable("userId") Integer userId) {
        return ResponseEntity.ok(modelMapper.map(userService.getUserById(userId), UserDto.class));
    }

    @GetMapping("/byUcn/{ucn}")
    public ResponseEntity<UserDto> getUserByUcn(@PathVariable("ucn") String ucn) {
        return ResponseEntity.ok(modelMapper.map(userService.getUserByUcn(ucn), UserDto.class));
    }

    @GetMapping("/roleById/{userId}")
    public ResponseEntity<String> getRoleByUserId(@PathVariable("userId") Integer userId) {
        return ResponseEntity.ok(userService.getRoleByUserId(userId));
    }

    @GetMapping("/roleByUcn/{ucn}")
    public ResponseEntity<String> getRoleByUcn(@PathVariable("ucn") String ucn) {
        return ResponseEntity.ok(userService.getRoleByUcn(ucn));
    }

    @GetMapping("/coaches")
    public ResponseEntity<List<CompetitorsAndCoachDto>> getAllCoaches() {
        return ResponseEntity.ok(userService.getAllCoaches().stream().map(user -> modelMapper.map(user, CompetitorsAndCoachDto.class)).toList());
    }

    @GetMapping("/competitors")
    public ResponseEntity<List<CompetitorsAndCoachDto>> getAllCompetitors() {
        return ResponseEntity.ok(userService.getAllCompetitors().stream().map(user -> modelMapper.map(user, CompetitorsAndCoachDto.class)).toList());
    }

    @GetMapping("/withCoaches/{userId}")
    public ResponseEntity<CompetitorsWithCoachesDto> getUsersWithCoaches(@PathVariable("userId") Integer userId) {
        Set<CompetitorsAndCoachDto> coaches = userService.getCoachesByUserId(userId).stream().map(coach -> modelMapper.map(coach, CompetitorsAndCoachDto.class)).collect(Collectors.toSet());
        CompetitorsWithCoachesDto user = modelMapper.map(userService.getUserById(userId), CompetitorsWithCoachesDto.class);
        user.setCoaches(coaches);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/competition/{userId}")
    public ResponseEntity<UsersWithRequestedCompetitionsDto> getUserWithCompetitions(@PathVariable("userId") Integer userId) {
        Set<CompetitionDto> competitions = userService.getCompetitionsByUserId(userId).stream().map(coach -> modelMapper.map(coach, CompetitionDto.class)).collect(Collectors.toSet());
        UsersWithRequestedCompetitionsDto user = modelMapper.map(userService.getUserById(userId), UsersWithRequestedCompetitionsDto.class);
        user.setCompetitions(competitions);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/byId/{userId}")
    public ResponseEntity<UserDto> deleteUserByUserId(@PathVariable("userId") Integer userId) {
        UserDto user = modelMapper.map(userService.deleteUserByUserId(userId, false), UserDto.class);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/byIdAdmin/{userId}")
    public ResponseEntity<UserDto> deleteUserByUserIdAdmin(@PathVariable("userId") Integer userId) {
        UserDto user =  modelMapper.map(userService.deleteUserByUserId(userId, true), UserDto.class);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/byUcn/{ucn}")
    public ResponseEntity<UserDto> deleteUserByUcn(@PathVariable("ucn") String ucn) {
        UserDto user =  modelMapper.map(userService.deleteUserByUcn(ucn, false), UserDto.class);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/byUcnAdmin/{ucn}")
    public ResponseEntity<UserDto> deleteUserByUcnAdmin(@PathVariable("ucn") String ucn) {
        UserDto user =  modelMapper.map(userService.deleteUserByUcn(ucn, true), UserDto.class);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/byUserId/{userId}")
    public ResponseEntity<UserDto> updateUserByUserId(@PathVariable("userId") Integer userId, @Valid @RequestBody UserUpdateDto newUser) {
        UserDto user =  modelMapper.map(userService.updateUserByUserId(userId, false, modelMapper.map(newUser, UserEntity.class)),  UserDto.class);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/byUserIdAdmin/{userId}")
    public ResponseEntity<UserDto> updateUserByUserIdAdmin(@PathVariable("userId") Integer userId, @Valid @RequestBody UserUpdateDto newUser) {
        UserDto user =  modelMapper.map(userService.updateUserByUserId(userId, true, modelMapper.map(newUser, UserEntity.class)), UserDto.class);
        return ResponseEntity.ok(user);
    }

    @PutMapping("byUcn/{ucn}")
    public ResponseEntity<UserDto> updateUserByUcn(@PathVariable("ucn") String ucn, @Valid @RequestBody UserUpdateDto newUser) {
        UserDto user =  modelMapper.map(userService.updateUserByUcn(ucn, false, modelMapper.map(newUser, UserEntity.class)), UserDto.class);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/byUcnAdmin/{ucn}")
    public ResponseEntity<UserDto> updateUserByUcnAdmin(@PathVariable("ucn") String ucn, @Valid @RequestBody UserUpdateDto newUser) {
        UserDto user =  modelMapper.map(userService.updateUserByUcn(ucn, true, modelMapper.map(newUser, UserEntity.class)), UserDto.class);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/leftClub/{userId}")
    public ResponseEntity<UserDto> leftClub(@PathVariable("userId") Integer userId) {
        return ResponseEntity.ok(modelMapper.map(userService.leftClub(userId), UserDto.class));
    }

    @PutMapping("makeCoach/{userId}")
    public ResponseEntity<UserDto> makeCoach(@PathVariable("userId") Integer userId) {
        return ResponseEntity.ok(modelMapper.map(userService.makeCoach(userId), UserDto.class));
    }

    @PutMapping("removeCoach/{userId}")
    public ResponseEntity<UserDto> removeCoach(@PathVariable("userId") Integer userId) {
        return ResponseEntity.ok(modelMapper.map(userService.removeCoach(userId), UserDto.class));
    }

    @GetMapping("getAllUsersInClub/{clubId}")
    public ResponseEntity<List<CompetitorsAndCoachDto>> getAllUsersInClub(@PathVariable("clubId") Integer clubId) {
        return ResponseEntity.ok(userService.getAllUsersInClub(clubId).stream().map(user -> modelMapper.map(user, CompetitorsAndCoachDto.class)).toList());
    }

    @GetMapping("getAllCompetitorsInClub/{clubId}")
    public ResponseEntity<List<CompetitorsAndCoachDto>> getAllCompetitorsInClub(@PathVariable("clubId") Integer clubId) {
        return ResponseEntity.ok(userService.getAllCompetitorsInClub(clubId).stream().map(user -> modelMapper.map(user, CompetitorsAndCoachDto.class)).toList());
    }

    @GetMapping("getAllCoachesInClub/{clubId}")
    public ResponseEntity<List<CompetitorsAndCoachDto>> getAllCoachesInClub(@PathVariable("clubId") Integer clubId) {
        return ResponseEntity.ok(userService.getAllCoachesInClub(clubId).stream().map(user -> modelMapper.map(user, CompetitorsAndCoachDto.class)).toList());
    }
}
