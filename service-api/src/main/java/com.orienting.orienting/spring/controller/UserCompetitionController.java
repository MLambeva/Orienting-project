package com.orienting.orienting.spring.controller;

import com.orienting.common.dto.UserDto;
import com.orienting.common.services.UserCompetitionService;
import lombok.Getter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/competitions")
@Getter
public class UserCompetitionController {
    private final UserCompetitionService userCompetitionService;
    private final ModelMapper modelMapper;

    @Autowired
    public UserCompetitionController(UserCompetitionService userCompetitionService, ModelMapper modelMapper) {
        this.userCompetitionService = userCompetitionService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/request/{userId}/{compId}")
    public ResponseEntity<String> requestParticipation(@PathVariable("userId") Integer userId, @PathVariable("compId")Integer compId) {
        userCompetitionService.requestParticipation(userId, compId);
        return ResponseEntity.ok(String.format("User with id %d requests participant in competition with id %d!", userId, compId));
    }

    @DeleteMapping("/remove/{userId}/{compId}")
    public ResponseEntity<UserDto> removeParticipation(@PathVariable("userId") Integer userId, @PathVariable("compId") Integer compId) {
        return ResponseEntity.ok(modelMapper.map(userCompetitionService.removeParticipation(userId, compId), UserDto.class));
    }
}