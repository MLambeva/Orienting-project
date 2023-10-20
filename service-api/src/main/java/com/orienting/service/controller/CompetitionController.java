package com.orienting.service.controller;

import com.orienting.common.dto.CompetitionDto;
import com.orienting.common.dto.CompetitionRequestDto;
import com.orienting.common.dto.CompetitionUpdateDto;
import com.orienting.common.dto.CompetitorDto;
import com.orienting.service.entity.CompetitionEntity;
import com.orienting.service.services.CompetitionService;
import jakarta.validation.Valid;
import lombok.Getter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "api/competitions", produces = MediaType.APPLICATION_JSON_VALUE)
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

    @GetMapping("/byDate/{date}")
    public ResponseEntity<List<CompetitionDto>> getCompetitionByDate(@PathVariable("date") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate date) {
        List<CompetitionDto> competitionDto = competitionService.getCompetitionByDate(date).stream()
                .map(competition -> modelMapper.map(competition, CompetitionDto.class))
                .toList();

        return ResponseEntity.ok(competitionDto);
    }

    @GetMapping("/withUsers/byDate/{date}")
    public ResponseEntity<List<CompetitionRequestDto>> getCompetitionByDateWithParticipants(@PathVariable("date") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate date) {
        List<CompetitionRequestDto> competitionDto =  competitionService.getCompetitionByDate(date).stream()
                .map(this::mapToCompDtoWithUsers)
                .toList();
        return ResponseEntity.ok(competitionDto);
    }

    @GetMapping("/byName/{name}")
    public ResponseEntity<CompetitionDto> getCompetitionByName(@PathVariable("name") String name) {
        CompetitionDto competitionDto = modelMapper.map(competitionService.getCompetitionByName(name), CompetitionDto.class);

        return ResponseEntity.ok(competitionDto);
    }

    @GetMapping("/withUsers/byName/{name}")
    public ResponseEntity<CompetitionRequestDto> getCompetitionByNameWithParticipants(@PathVariable("name") String name) {
        CompetitionRequestDto competitionDto = modelMapper.map(competitionService.getCompetitionByName(name), CompetitionRequestDto.class);

        return ResponseEntity.ok(competitionDto);
    }

    @GetMapping("/byCompId/{compId}")
    public ResponseEntity<CompetitionDto> getCompetitionByName(@PathVariable("compId") Integer compId) {
        CompetitionDto competitionDto = modelMapper.map(competitionService.getCompetitionByCompId(compId), CompetitionDto.class);

        return ResponseEntity.ok(competitionDto);
    }
    @GetMapping("/withUsers/byCompId/{compId}")
    public ResponseEntity<CompetitionRequestDto> getCompetitionByNameWithParticipants(@PathVariable("compId") Integer compId) {
        CompetitionRequestDto competitionDto = modelMapper.map(competitionService.getCompetitionByCompId(compId), CompetitionRequestDto.class);

        return ResponseEntity.ok(competitionDto);
    }

    @PostMapping("/add")
    public ResponseEntity<CompetitionDto> createCompetition(@RequestBody @Valid CompetitionDto competition) {
        CompetitionEntity competitionEntity = competitionService.createCompetition(modelMapper.map(competition, CompetitionEntity.class));
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path("/{compId}")
                .buildAndExpand(competition.getCompId())
                .toUri();
        return ResponseEntity.created(uri).body(modelMapper.map(competitionEntity, CompetitionDto.class));
    }

    @DeleteMapping("delete/byCompId/{compId}")
    public ResponseEntity<CompetitionDto> deleteCompetitionById(@PathVariable("compId") Integer compId) {
        return ResponseEntity.ok(modelMapper.map(competitionService.deleteCompById(compId), CompetitionDto.class));
    }

    @DeleteMapping("delete/byName/{name}")
    public ResponseEntity<CompetitionDto> deleteCompetitionByName(@PathVariable("name") String name) {
        return ResponseEntity.ok(modelMapper.map(competitionService.deleteCompByName(name), CompetitionDto.class));
    }

    @PutMapping("update/byCompId/{compId}")
    public ResponseEntity<CompetitionDto> updateCompetitionByCompId(@PathVariable("compId") Integer compId, @RequestBody @Valid CompetitionUpdateDto competition) {
        return ResponseEntity.ok(modelMapper.map(competitionService.updateCompetitionById(compId, modelMapper.map(competition, CompetitionEntity.class)), CompetitionDto.class));
    }

    @PutMapping("update/byName/{name}")
    public ResponseEntity<CompetitionDto> updateCompetitionByName(@PathVariable("name") String name, @RequestBody @Valid CompetitionUpdateDto competition) {
        return ResponseEntity.ok(modelMapper.map(competitionService.updateCompetitionByName(name, modelMapper.map(competition, CompetitionEntity.class)), CompetitionDto.class));
    }
}
