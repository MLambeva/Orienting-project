package com.orienting.orienting.spring.controller;

import com.orienting.common.dto.ClubDto;
import com.orienting.common.dto.UserDto;
import com.orienting.common.entity.ClubEntity;
import com.orienting.common.entity.UserEntity;
import com.orienting.common.services.UserClubService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api")
public class UserClubController {
    private final UserClubService userClubService;
    private final ModelMapper modelMapper;

    @Autowired
    public UserClubController(UserClubService clubService, ModelMapper modelMapper) {
        this.userClubService = clubService;
        this.modelMapper = modelMapper;
    }
    @GetMapping("/allClubs")
    public ResponseEntity<List<ClubDto>> getAllClubs() {
        List<ClubDto> clubDto = userClubService.getAllClubs().stream()
                .map(club -> modelMapper.map(club, ClubDto.class))
                .toList();

        return ResponseEntity.ok(clubDto);
    }

    @GetMapping("/usersByClub/{clubId}")
    public  ResponseEntity<List<UserDto>> getUsersByClubId(@PathVariable("clubId") Integer clubId) {
        List<UserDto> users = userClubService.getAllUsersByClubId(clubId).stream()
                .map(user -> modelMapper.map(user, UserDto.class)).toList();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/UsersByCoach/{coachId}")
    public  ResponseEntity<List<UserDto>> getUsersByCoachId(@PathVariable("coachId") Integer coachId) {
        List<UserDto> users = userClubService.getAllUsersByCoachId(coachId).stream()
                .map(user -> modelMapper.map(user, UserDto.class)).toList();
        return ResponseEntity.ok(users);
    }

    @PostMapping("/addClub")
    public ResponseEntity<String> createClub(@RequestBody @Valid ClubDto clubDto) {
        ClubEntity club = userClubService.createClub(modelMapper.map(clubDto, ClubEntity.class));
        return ResponseEntity.ok(String.format("Club with id: %d was created successfully", club.getClubId()));
    }

    @PostMapping("/addUser")
    public ResponseEntity<String> createUser(@RequestBody @Valid UserDto userDto) {
        UserEntity user = userClubService.createUser(modelMapper.map(userDto, UserEntity.class));
        userClubService.updateClub(user);
        return ResponseEntity.ok(String.format("User with id: %d was added!", user.getUserId()));
    }

}
