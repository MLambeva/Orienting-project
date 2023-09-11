package com.orienting.orienting.spring.controller;

import com.orienting.common.dto.*;
import com.orienting.common.entity.ClubEntity;
import com.orienting.common.entity.UserEntity;
import com.orienting.common.services.ClubService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/clubs")
public class ClubController {
    private final ClubService clubService;
    private final ModelMapper modelMapper;

    @Autowired
    public ClubController(ClubService clubService, ModelMapper modelMapper) {
        this.clubService = clubService;
        this.modelMapper = modelMapper;
    }

    private ClubWithUsersDto mapToClubDtoWithUsers(ClubEntity clubEntity) {
        ClubWithUsersDto clubDto = modelMapper.map(clubEntity, ClubWithUsersDto.class);
        Set<UserDto> competitorsDto = clubEntity.getUsers().stream()
                .filter(UserEntity::isCompetitor)
                .map(userEntity -> modelMapper.map(userEntity, UserDto.class))
                .collect(Collectors.toSet());
        Set<CoachDto> coachDto = clubEntity.getUsers().stream()
                .filter(UserEntity::isCoach)
                .map(userEntity -> modelMapper.map(userEntity, CoachDto.class))
                .collect(Collectors.toSet());
        clubDto.setCompetitors(competitorsDto);
        clubDto.setCoaches(coachDto);
        return clubDto;
    }

    @GetMapping("/all")
    public ResponseEntity<List<ClubDto>> getAllClubs() {
        return ResponseEntity.ok(clubService.getAllClubs().stream()
                .map(club -> modelMapper.map(club, ClubDto.class))
                .toList());
    }

    @GetMapping("/allWithUsers")
    public ResponseEntity<List<ClubWithUsersDto>> getAllClubsWithUsers() {
        return ResponseEntity.ok(clubService.getAllClubs().stream()
                .map(this::mapToClubDtoWithUsers)
                .collect(Collectors.toList()));
    }

    @GetMapping("/{clubId}")
    public ResponseEntity<ClubDto> getClubByClubId(@PathVariable("clubId") Integer clubId) {
        return ResponseEntity.ok(modelMapper.map(clubService.getClubById(clubId), ClubDto.class));
    }

    @GetMapping("/withUsers/{clubId}")
    public ResponseEntity<ClubWithUsersDto> getClubByClubIdWithUsers(@PathVariable("clubId") Integer clubId) {
        return ResponseEntity.ok(mapToClubDtoWithUsers(clubService.getClubById(clubId)));
    }

    @PostMapping("/add")
    public ResponseEntity<String> addClub(@RequestBody @Valid ClubDto clubDto) {
        ClubEntity club = clubService.createClub(modelMapper.map(clubDto, ClubEntity.class));
        return ResponseEntity.ok(String.format("Club with id: %d was created successfully", club.getClubId()));
    }

    @DeleteMapping("/delete/{clubId}")
    public ResponseEntity<String> deleteClub(@PathVariable("clubId") Integer clubId) {
        clubService.deleteClub(clubId);
        return ResponseEntity.ok(String.format("Club with clubId %d was deleted!", clubId));
    }

    @PutMapping("update/{clubId}")
    public ResponseEntity<String> update(@PathVariable("clubId") Integer clubId, @Valid @RequestBody ClubUpdateDto newClub) {
        ClubEntity club = modelMapper.map(newClub, ClubEntity.class);
        clubService.updateClub(clubId, club);
        return ResponseEntity.ok(String.format("Club with clubId %d was updated!", clubId));
    }

}
