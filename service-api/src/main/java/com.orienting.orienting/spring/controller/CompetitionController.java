package com.orienting.orienting.spring.controller;

import com.orienting.common.dto.*;
import com.orienting.common.entity.CompetitionEntity;
import com.orienting.common.services.CompetitionService;
import jakarta.validation.Valid;
import lombok.Getter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/competitions")
@Getter
public class CompetitionController {
    private final CompetitionService competitionService;
    private final ModelMapper modelMapper;

    @Autowired
    public CompetitionController(CompetitionService competitionService, ModelMapper modelMapper) {
        this.competitionService = competitionService;
        this.modelMapper = modelMapper;
    }

    private CompetitionRequestDto mapToCompDtoWithUsers(CompetitionEntity competitionEntity) {
        CompetitionRequestDto compDto = modelMapper.map(competitionEntity, CompetitionRequestDto.class);
        Set<CompetitorDto> competitionsDto = competitionEntity.getUsers().stream()
                .map(userEntity -> modelMapper.map(userEntity, CompetitorDto.class))
                .collect(Collectors.toSet());

        compDto.setUsers(competitionsDto);
        return compDto;
    }

    @GetMapping("/all")
    public ResponseEntity<List<CompetitionDto>> getAllCompetitions() {
        List<CompetitionDto> competitionDto = competitionService.getAllCompetitions().stream()
                .map(competition -> modelMapper.map(competition, CompetitionDto.class))
                .collect(Collectors.toList());

        return ResponseEntity.ok(competitionDto);
    }

    @GetMapping("/allWithParticipants")
    public ResponseEntity<List<CompetitionRequestDto>> getAllCompetitionsWithParticipants() {
        return ResponseEntity.ok(competitionService.getAllCompetitions().stream()
                .map(this::mapToCompDtoWithUsers)
                .collect(Collectors.toList()));
    }

    @GetMapping("/{date}")
    public ResponseEntity<List<CompetitionDto>> getCompetitionByDate(@PathVariable("date") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate date) {
        List<CompetitionDto> competitionDto = competitionService.getCompetitionByDate(date).stream()
                .map(competition -> modelMapper.map(competition, CompetitionDto.class))
                .toList();

        return ResponseEntity.ok(competitionDto);
    }

    @PostMapping
    public ResponseEntity<CompetitionDto> createCompetition(@RequestBody @Valid CompetitionDto competition) {
        CompetitionEntity competitionEntity = competitionService.createCompetition(modelMapper.map(competition, CompetitionEntity.class));
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path("/{compId}")
                .buildAndExpand(competition.getCompId())
                .toUri();
        return ResponseEntity.created(uri).body(modelMapper.map(competitionEntity, CompetitionDto.class));
    }

    @DeleteMapping("/{compId}")
    public ResponseEntity<CompetitionDto> deleteCompetitionById(@PathVariable("compId") Integer compId) {
        return ResponseEntity.ok(modelMapper.map(competitionService.deleteCompById(compId), CompetitionDto.class));
    }

    @PutMapping("update/{compId}")
    public ResponseEntity<CompetitionDto> updateCompetitionByCompId(@PathVariable("compId") Integer compId, @RequestBody @Valid CompetitionUpdateDto competition) {
        return ResponseEntity.ok(modelMapper.map(competitionService.updateCompetition(compId, modelMapper.map(competition, CompetitionEntity.class)), CompetitionDto.class));
    }
}
