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
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/clubs")
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
                .collect(Collectors.toList());

        return ResponseEntity.ok(clubDto);
    }

    @PostMapping("/club")
    public ResponseEntity<String> createClub(@RequestBody @Valid ClubDto clubDto) {
        ClubEntity club = modelMapper.map(clubDto, ClubEntity.class);
        clubService.createClub(club);
        return ResponseEntity.ok("Club created successfully");
    }
}
