package com.orienting.orienting.spring.controller;

import com.orienting.common.dto.*;
import com.orienting.common.entity.ClubEntity;
import com.orienting.common.entity.UserEntity;
import com.orienting.common.services.ClubService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/clubs", produces = MediaType.APPLICATION_JSON_VALUE)
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
        Set<CompetitorsAndCoachDto> competitorsDto = clubEntity.getUsers().stream()
                .filter(UserEntity::isCompetitor)
                .map(userEntity -> modelMapper.map(userEntity, CompetitorsAndCoachDto.class))
                .collect(Collectors.toSet());
        Set<CompetitorsAndCoachDto> coachDto = clubEntity.getUsers().stream()
                .filter(UserEntity::isCoach)
                .map(userEntity -> modelMapper.map(userEntity, CompetitorsAndCoachDto.class))
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

    @GetMapping("/byId/{clubId}")
    public ResponseEntity<ClubDto> getClubById(@PathVariable("clubId") Integer clubId) {
        return ResponseEntity.ok(modelMapper.map(clubService.getClubById(clubId), ClubDto.class));
    }
    
    @GetMapping("/byName/{clubName}")
    public ResponseEntity<ClubDto> getClubByName(@PathVariable("clubName") String clubName) {
        return ResponseEntity.ok(modelMapper.map(clubService.getClubByName(clubName), ClubDto.class));
    }

    @GetMapping("/withUsersById/{clubId}")
    public ResponseEntity<ClubWithUsersDto> getClubWithUsersById(@PathVariable("clubId") Integer clubId) {
        return ResponseEntity.ok(mapToClubDtoWithUsers(clubService.getClubById(clubId)));
    }

    @GetMapping("/withUsersByName/{clubName}")
    public ResponseEntity<ClubWithUsersDto> getClubWithUsersByName(@PathVariable("clubName") String clubName) {
        return ResponseEntity.ok(mapToClubDtoWithUsers(clubService.getClubByName(clubName)));
    }

    @PostMapping("/add")
    public ResponseEntity<ClubDto> addClub(@RequestBody @Valid ClubDto clubDto) {
        ClubEntity club = clubService.createClub(modelMapper.map(clubDto, ClubEntity.class));
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path("/{clubId}")
                .buildAndExpand(clubDto.getClubId())
                .toUri();
        return ResponseEntity.created(uri)
                .body(modelMapper.map(club, ClubDto.class));
    }

    @DeleteMapping("/deleteById/{clubId}")
    public ResponseEntity<ClubDto> deleteClubById(@PathVariable("clubId") Integer clubId) {
        return ResponseEntity.ok(modelMapper.map(clubService.deleteClubById(clubId), ClubDto.class));
    }

    @DeleteMapping("/deleteByName/{clubName}")
    public ResponseEntity<ClubDto> deleteClubByName(@PathVariable("clubName") String clubName) {
        return ResponseEntity.ok(modelMapper.map(clubService.deleteClubByName(clubName), ClubDto.class));
    }

    @PutMapping("/updateById/{clubId}")
    public ResponseEntity<ClubDto> updateClubById(@PathVariable("clubId") Integer clubId, @RequestBody ClubDto newClub) {
        ClubEntity club = modelMapper.map(newClub, ClubEntity.class);
        return ResponseEntity.ok(modelMapper.map(clubService.updateClubById(clubId, club), ClubDto.class));
    }

    @PutMapping("/updateByName/{clubName}")
    public ResponseEntity<ClubDto> updateClubByName(@PathVariable("clubName") String clubName, @RequestBody ClubDto newClub) {
        ClubEntity club = modelMapper.map(newClub, ClubEntity.class);
        return ResponseEntity.ok(modelMapper.map(clubService.updateClubByName(clubName, club), ClubDto.class));
    }
}
