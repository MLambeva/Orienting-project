package com.orienting.service.controller;

import com.orienting.common.dto.*;
import com.orienting.common.entity.UserEntity;
import com.orienting.common.services.UserService;
import lombok.Getter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/users", produces = MediaType.APPLICATION_JSON_VALUE)
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

    @GetMapping("/me")
    public ResponseEntity<UserDto> getAuthenticatedUser(Authentication authentication) {
        return getUserByUserId(userService.findAuthenticatedUser(authentication.getName()).getUserId());
    }

    @GetMapping("/byUcn/{ucn}")
    public ResponseEntity<UserDto> getUserByUcn(@PathVariable("ucn") String ucn) {
        return ResponseEntity.ok(modelMapper.map(userService.getUserByUcn(ucn), UserDto.class));
    }

    @GetMapping("/role/byId/{userId}")
    public ResponseEntity<String> getRoleByUserId(@PathVariable("userId") Integer userId) {
        return ResponseEntity.ok(userService.getRoleByUserId(userId));
    }

    @GetMapping("/role")
    public ResponseEntity<String> getRoleByUserIdMe(Authentication authentication) {
        return getRoleByUserId(userService.findAuthenticatedUser(authentication.getName()).getUserId());
    }

    @GetMapping("/role/byUcn/{ucn}")
    public ResponseEntity<String> getRoleByUcn(@PathVariable("ucn") String ucn) {
        return ResponseEntity.ok(userService.getRoleByUcn(ucn));
    }

    @GetMapping("/allCoaches")
    public ResponseEntity<List<CompetitorDto>> getAllCoaches() {
        return ResponseEntity.ok(userService.getAllCoaches().stream().map(user -> modelMapper.map(user, CompetitorDto.class)).toList());
    }

    @GetMapping("/allCompetitors")
    public ResponseEntity<List<CompetitorDto>> getAllCompetitors() {
        return ResponseEntity.ok(userService.getAllCompetitors().stream().map(user -> modelMapper.map(user, CompetitorDto.class)).toList());
    }

    @GetMapping("/withCoaches/{userId}")
    public ResponseEntity<CompetitorsWithCoachesDto> getUsersWithCoaches(@PathVariable("userId") Integer userId) {
        Set<CompetitorsAndCoachDto> coaches = userService.getCoachesByUserId(userId).stream().map(coach -> modelMapper.map(coach, CompetitorsAndCoachDto.class)).collect(Collectors.toSet());
        CompetitorsWithCoachesDto user = modelMapper.map(userService.getUserById(userId), CompetitorsWithCoachesDto.class);
        user.setCoaches(coaches);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/withCoaches")
    public ResponseEntity<CompetitorsWithCoachesDto> getMeWithCoaches(Authentication authentication) {
        UserEntity user = userService.findAuthenticatedUser(authentication.getName());
        return getUsersWithCoaches(user.getUserId());
    }

    @GetMapping("/competition/{userId}") // information about user and all requested competitions
    public ResponseEntity<UsersWithRequestedCompetitionsDto> getUserWithRequestedCompetitions(@PathVariable("userId") Integer userId) {
        Set<CompetitionDto> competitions = userService.getCompetitionsByUserId(userId).stream().map(coach -> modelMapper.map(coach, CompetitionDto.class)).collect(Collectors.toSet());
        UsersWithRequestedCompetitionsDto user = modelMapper.map(userService.getUserById(userId), UsersWithRequestedCompetitionsDto.class);
        user.setCompetitions(competitions);
        return ResponseEntity.ok(user);
    }


    @GetMapping("/competition")
    public ResponseEntity<UsersWithRequestedCompetitionsDto> getMeWithRequestedCompetitions(Authentication authentication) {
        UserEntity user = userService.findAuthenticatedUser(authentication.getName());
        return getUserWithRequestedCompetitions(user.getUserId());
    }

    @GetMapping("allUsersInClub/byId/{clubId}")
    public ResponseEntity<List<CompetitorsAndCoachDto>> getAllUsersInClubById(@PathVariable("clubId") Integer clubId) {
        return ResponseEntity.ok(userService.getAllUsersInClubByClubId(clubId).stream().map(user -> modelMapper.map(user, CompetitorsAndCoachDto.class)).toList());
    }

    @GetMapping("allCompetitorsInClub/byId/{clubId}")
    public ResponseEntity<List<CompetitorsAndCoachDto>> getAllCompetitorsInClubById(@PathVariable("clubId") Integer clubId) {
        return ResponseEntity.ok(userService.getAllCompetitorsInClubByClubId(clubId).stream().map(user -> modelMapper.map(user, CompetitorsAndCoachDto.class)).toList());
    }

    @GetMapping("allCoachesInClub/byId/{clubId}")
    public ResponseEntity<List<CompetitorsAndCoachDto>> getAllCoachesInClubById(@PathVariable("clubId") Integer clubId) {
        return ResponseEntity.ok(userService.getAllCoachesInClubByClubId(clubId).stream().map(user -> modelMapper.map(user, CompetitorsAndCoachDto.class)).toList());
    }

    @GetMapping("allUsersInClub/byName/{clubName}")
    public ResponseEntity<List<CompetitorsAndCoachDto>> getAllUsersInClubByName(@PathVariable("clubName") String clubName) {
        return ResponseEntity.ok(userService.getAllUsersInClubByClubName(clubName).stream().map(user -> modelMapper.map(user, CompetitorsAndCoachDto.class)).toList());
    }

    @GetMapping("allCompetitorsInClub/byName/{clubName}")
    public ResponseEntity<List<CompetitorsAndCoachDto>> getAllCompetitorsInClubByNAme(@PathVariable("clubName") String clubName) {
        return ResponseEntity.ok(userService.getAllCompetitorsInClubByClubName(clubName).stream().map(user -> modelMapper.map(user, CompetitorsAndCoachDto.class)).toList());
    }

    @GetMapping("allCoachesInClub/byName/{clubName}")
    public ResponseEntity<List<CompetitorsAndCoachDto>> getAllCoachesInClubByName(@PathVariable("clubName") String clubName) {
        return ResponseEntity.ok(userService.getAllCoachesInClubByClubName(clubName).stream().map(user -> modelMapper.map(user, CompetitorsAndCoachDto.class)).toList());
    }

    @GetMapping("competitorsAndCoaches")
    public ResponseEntity<AllUsersInClubDto> getAllUsersInClub(Authentication authentication) {
        if(userService.findAuthenticatedUser(authentication.getName()).getClub() == null) {
            throw new IllegalArgumentException("User does not belong to club!");
        }
        Integer clubId = userService.findAuthenticatedUser(authentication.getName()).getClub().getClubId();
        AllUsersInClubDto result = new AllUsersInClubDto();
        result.setCompetitors(userService.getAllCompetitorsInClubByClubId(clubId).stream().map(coach -> modelMapper.map(coach, CompetitorsAndCoachDto.class)).collect(Collectors.toSet()));
        result.setCoaches(userService.getAllCoachesInClubByClubId(clubId).stream().map(coach -> modelMapper.map(coach, CompetitorsAndCoachDto.class)).collect(Collectors.toSet()));

        return ResponseEntity.ok(result);
    }

    @GetMapping("/coaches")
    public ResponseEntity<List<CompetitorsAndCoachDto>> getAllCoachesInClub(Authentication authentication) {
        return getAllCoachesInClubById(userService.findAuthenticatedUser(authentication.getName()).getClub().getClubId());
    }

    @GetMapping("/competitors")
    public ResponseEntity<List<CompetitorsAndCoachDto>> getAllCompetitorsInClub(Authentication authentication) {
        return getAllCompetitorsInClubById(userService.findAuthenticatedUser(authentication.getName()).getClub().getClubId());
    }

    @GetMapping("/club")
    public ResponseEntity<ClubDto> getLoginUserClub(Authentication authentication) {
        return ResponseEntity.ok(modelMapper.map(userService.findAuthenticatedUser(authentication.getName()).getClub(), ClubDto.class));
    }

    @DeleteMapping("/remove/{userId}") // Admins & coaches
    public ResponseEntity<UserDto> deleteUser(@PathVariable("userId") Integer userId) {
        UserDto user = modelMapper.map(userService.deleteUser(userId), UserDto.class);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/remove/byId/{userId}") // Admins & coaches
    public ResponseEntity<UserDto> deleteUserByUserId(@PathVariable("userId") Integer userId, Authentication authentication) {
        UserDto user = modelMapper.map(userService.deleteUserByUserId(userId, authentication.getName()), UserDto.class);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/remove/byUcn/{ucn}") // Admins & coaches
    public ResponseEntity<UserDto> deleteUserByUcn(@PathVariable("ucn") String ucn, Authentication authentication) {
        UserDto user = modelMapper.map(userService.deleteUserByUcn(ucn, authentication.getName()), UserDto.class);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/leftClub/{userId}")
    public ResponseEntity<UserDto> leftClub(@PathVariable("userId") Integer userId) {
        return ResponseEntity.ok(modelMapper.map(userService.leftClub(userId), UserDto.class));
    }

    @PutMapping("/leftClub")
    public ResponseEntity<UserDto> leftClub(Authentication authentication) {
        return leftClub(userService.findAuthenticatedUser(authentication.getName()).getUserId());
    }

    @PutMapping("makeCoach/{userId}")
    public ResponseEntity<UserDto> makeCoach(@PathVariable("userId") Integer userId) {
        return ResponseEntity.ok(modelMapper.map(userService.makeCoach(userId), UserDto.class));
    }

    @PutMapping("removeCoach/{userId}")
    public ResponseEntity<UserDto> removeCoach(@PathVariable("userId") Integer userId) {
        return ResponseEntity.ok(modelMapper.map(userService.removeCoach(userId), UserDto.class));
    }
}
