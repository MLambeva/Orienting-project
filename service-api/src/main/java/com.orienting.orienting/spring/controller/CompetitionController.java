package com.orienting.orienting.spring.controller;

import com.orienting.common.dto.CompetitionDto;
import com.orienting.common.entity.CompetitionEntity;
import com.orienting.common.services.CompetitionService;
import jakarta.validation.Valid;
import lombok.Getter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
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

    @GetMapping("/all")
    public ResponseEntity<List<CompetitionDto>> getAllCompetitions() {
        List<CompetitionDto> competitionDto = competitionService.getAllCompetitions().stream()
                .map(competition -> modelMapper.map(competition, CompetitionDto.class))
                .collect(Collectors.toList());

        return ResponseEntity.ok(competitionDto);
    }

    @GetMapping("/{date}")
    public ResponseEntity<List<CompetitionDto>> getCompetitionByDate(@PathVariable("date") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate date) {
        List<CompetitionDto> competitionDto = competitionService.getCompetitionByDate(date).stream()
                .map(competition -> modelMapper.map(competition, CompetitionDto.class))
                .toList();

        return ResponseEntity.ok(competitionDto);
    }

    @PostMapping
    public ResponseEntity<String> createCompetition(@RequestBody @Valid CompetitionDto competition) {
        CompetitionEntity competitionEntity = modelMapper.map(competition, CompetitionEntity.class);
        competitionService.createCompetition(competitionEntity);
        return ResponseEntity.ok(String.format("Competition with id: %d was added!", competitionEntity.getCompId()));
    }

    @DeleteMapping("/{compId}")
    public ResponseEntity<String> deleteCompetitionById(@PathVariable("compId") Integer compId) {
        competitionService.deleteCompById(compId);
        return ResponseEntity.ok(String.format("Competition with id: %s was deleted!", compId));
    }

}
