package com.orienting.orienting.spring.controller;

import com.orienting.common.dto.UserDto;
import com.orienting.common.services.UserCompetitionService;
import lombok.Getter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

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
    public ResponseEntity<UserDto> requestParticipation(@PathVariable("userId") Integer userId, @PathVariable("compId")Integer compId) {
        UserDto user = modelMapper.map(userCompetitionService.requestParticipation(userId, compId), UserDto.class);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path("/{userId}" + "/{compId}")
                .buildAndExpand(userId, compId)
                .toUri();
        return ResponseEntity.created(uri)
                .body(user);
    }

    @PostMapping("/request/{compId}")
    public ResponseEntity<UserDto> requestParticipation(@PathVariable("compId")Integer compId) {
        UserDto user = modelMapper.map(userCompetitionService.userRequestParticipation(compId), UserDto.class);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path("/{userId}" + "/{compId}")
                .buildAndExpand(user.getUserId(), compId)
                .toUri();
        return ResponseEntity.created(uri)
                .body(user);
    }

    @DeleteMapping("/remove/{userId}/{compId}")
    public ResponseEntity<UserDto> removeParticipation(@PathVariable("userId") Integer userId, @PathVariable("compId") Integer compId) {
        return ResponseEntity.ok(modelMapper.map(userCompetitionService.removeParticipation(userId, compId), UserDto.class));
    }
}
