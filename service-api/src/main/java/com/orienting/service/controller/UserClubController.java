package com.orienting.service.controller;

import com.orienting.common.dto.*;
import com.orienting.service.entity.UserEntity;
import com.orienting.service.exception.NoExistedClubException;
import com.orienting.service.services.UserClubService;
import jakarta.validation.Valid;
import lombok.Getter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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

    @GetMapping("allUsersInClub/byId/{clubId}")
    public ResponseEntity<List<CompetitorsAndCoachDto>> getAllUsersInClubById(@PathVariable("clubId") Integer clubId) {
        return ResponseEntity.ok(userClubService.getAllUsersInClubByClubId(clubId).stream().map(user -> modelMapper.map(user, CompetitorsAndCoachDto.class)).toList());
    }

    @GetMapping("allCompetitorsInClub/byId/{clubId}")
    public ResponseEntity<List<CompetitorsAndCoachDto>> getAllCompetitorsInClubById(@PathVariable("clubId") Integer clubId) {
        return ResponseEntity.ok(userClubService.getAllCompetitorsInClubByClubId(clubId).stream().map(user -> modelMapper.map(user, CompetitorsAndCoachDto.class)).toList());
    }

    @GetMapping("allCoachesInClub/byId/{clubId}")
    public ResponseEntity<List<CompetitorsAndCoachDto>> getAllCoachesInClubById(@PathVariable("clubId") Integer clubId) {
        return ResponseEntity.ok(userClubService.getAllCoachesInClubByClubId(clubId).stream().map(user -> modelMapper.map(user, CompetitorsAndCoachDto.class)).toList());
    }

    @GetMapping("allUsersInClub/byName/{clubName}")
    public ResponseEntity<List<CompetitorsAndCoachDto>> getAllUsersInClubByName(@PathVariable("clubName") String clubName) {
        return ResponseEntity.ok(userClubService.getAllUsersInClubByClubName(clubName).stream().map(user -> modelMapper.map(user, CompetitorsAndCoachDto.class)).toList());
    }

    @GetMapping("allCompetitorsInClub/byName/{clubName}")
    public ResponseEntity<List<CompetitorsAndCoachDto>> getAllCompetitorsInClubByNAme(@PathVariable("clubName") String clubName) {
        return ResponseEntity.ok(userClubService.getAllCompetitorsInClubByClubName(clubName).stream().map(user -> modelMapper.map(user, CompetitorsAndCoachDto.class)).toList());
    }

    @GetMapping("allCoachesInClub/byName/{clubName}")
    public ResponseEntity<List<CompetitorsAndCoachDto>> getAllCoachesInClubByName(@PathVariable("clubName") String clubName) {
        return ResponseEntity.ok(userClubService.getAllCoachesInClubByClubName(clubName).stream().map(user -> modelMapper.map(user, CompetitorsAndCoachDto.class)).toList());
    }

    private void validateIfHaveClub(Authentication authentication) {
        if(userClubService.findAuthenticatedUser(authentication.getName()).getClub() == null) {
            throw new NoExistedClubException("User does not belong to club!");
        }
    }
    @GetMapping("competitorsAndCoaches")
    public ResponseEntity<AllUsersInClubDto> getAllUsersInClub(Authentication authentication) {
        validateIfHaveClub(authentication);
        Integer clubId = userClubService.findAuthenticatedUser(authentication.getName()).getClub().getClubId();
        AllUsersInClubDto result = new AllUsersInClubDto();
        result.setCompetitors(userClubService.getAllCompetitorsInClubByClubId(clubId).stream().map(coach -> modelMapper.map(coach, CompetitorsAndCoachDto.class)).collect(Collectors.toSet()));
        result.setCoaches(userClubService.getAllCoachesInClubByClubId(clubId).stream().map(coach -> modelMapper.map(coach, CompetitorsAndCoachDto.class)).collect(Collectors.toSet()));

        return ResponseEntity.ok(result);
    }

    @GetMapping("/coaches")
    public ResponseEntity<List<CompetitorsAndCoachDto>> getAllCoachesInClub(Authentication authentication) {
        validateIfHaveClub(authentication);
        return getAllCoachesInClubById(userClubService.findAuthenticatedUser(authentication.getName()).getClub().getClubId());
    }

    @GetMapping("/competitors")
    public ResponseEntity<List<CompetitorsAndCoachDto>> getAllCompetitorsInClub(Authentication authentication) {
        validateIfHaveClub(authentication);
        return getAllCompetitorsInClubById(userClubService.findAuthenticatedUser(authentication.getName()).getClub().getClubId());
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
    public ResponseEntity<UserDto> addClubToLoggedInUser(@PathVariable("clubId") Integer clubId, Authentication authentication) {
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
    public ResponseEntity<UserDto> updateLoggedInUser(@Valid @RequestBody UserUpdateDto newUser, Authentication authentication) {
        UserDto user = modelMapper.map(userClubService.update(authentication.getName(), modelMapper.map(newUser, UserEntity.class)), UserDto.class);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/addUser")
    public ResponseEntity<UserDto> addUser(@Valid @RequestBody UserCreationDto user) {
        UserDto userDto = modelMapper.map(userClubService.addUser(modelMapper.map(user, UserEntity.class)), UserDto.class);
        return ResponseEntity.ok(userDto);
    }
}
