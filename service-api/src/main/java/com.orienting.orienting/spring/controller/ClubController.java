package com.orienting.orienting.spring.controller;

import com.orienting.common.dto.ClubDto;
import com.orienting.common.entity.ClubEntity;
import com.orienting.common.services.ClubService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @GetMapping("/all")
    public ResponseEntity<List<ClubDto>> getAllClubs() {
        List<ClubDto> clubDto = clubService.getAllClubs().stream()
                .map(club -> modelMapper.map(club, ClubDto.class))
                .toList();


        return ResponseEntity.ok(clubDto);
    }

    @GetMapping("/{clubId}")
    public ResponseEntity<ClubDto> getClubByClubId(@PathVariable("clubId") Integer clubId) {
        return ResponseEntity.ok(modelMapper.map(clubService.getClubById(clubId), ClubDto.class));
    }

    @PostMapping("/clubs/add")
    public ResponseEntity<String> addClub(@RequestBody @Valid ClubDto clubDto) {
        ClubEntity club = clubService.createClub(modelMapper.map(clubDto, ClubEntity.class));
        return ResponseEntity.ok(String.format("Club with id: %d was created successfully", club.getClubId()));
    }

}
