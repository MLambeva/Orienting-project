package com.orienting.service.controller;

import com.orienting.common.dto.UserDto;
import com.orienting.common.exception.UnauthorizedException;
import com.orienting.common.services.UserCompetitionService;
import lombok.Getter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/competitions", produces = MediaType.APPLICATION_JSON_VALUE)
@Getter
public class UserCompetitionController {
    private final UserCompetitionService userCompetitionService;
    private final ModelMapper modelMapper;

    @Autowired
    public UserCompetitionController(UserCompetitionService userCompetitionService, ModelMapper modelMapper) {
        this.userCompetitionService = userCompetitionService;
        this.modelMapper = modelMapper;
    }

    @PutMapping("/request/byId/{userId}/{compId}")
    public ResponseEntity<UserDto> requestParticipationById(@PathVariable("userId") Integer userId, @PathVariable("compId")Integer compId, Authentication authentication) {
        UserDto user = modelMapper.map(userCompetitionService.requestParticipation(userId, compId, authentication.getName()), UserDto.class);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/request/byName/{userId}/{name}")
    public ResponseEntity<UserDto> requestParticipationByName(@PathVariable("userId") Integer userId, @PathVariable("name")String name, Authentication authentication) {
        UserDto user = modelMapper.map(userCompetitionService.requestParticipationByName(userId, name, authentication.getName()), UserDto.class);
        return ResponseEntity.ok(user);
    }
    @PutMapping("/request/byId/{compId}")
    public ResponseEntity<UserDto> requestParticipationById(@PathVariable("compId")Integer compId, Authentication authentication) throws UnauthorizedException {
        return requestParticipationById(userCompetitionService.findAuthenticatedUser(authentication.getName()).getUserId(), compId, authentication);
    }

    @PutMapping("/request/byName/{name}")
    public ResponseEntity<UserDto> requestParticipationByName(@PathVariable("name")String name, Authentication authentication) throws UnauthorizedException {
        return requestParticipationByName(userCompetitionService.findAuthenticatedUser(authentication.getName()).getUserId(), name, authentication);
    }

    @PutMapping("/remove/byId/{userId}/{compId}")
    public ResponseEntity<UserDto> removeParticipationById(@PathVariable("userId") Integer userId, @PathVariable("compId") Integer compId, Authentication authentication) {
        return ResponseEntity.ok(modelMapper.map(userCompetitionService.removeParticipationById(userId, compId, authentication.getName()), UserDto.class));
    }

    @PutMapping("/remove/byName/{userId}/{name}")
    public ResponseEntity<UserDto> removeParticipationByName(@PathVariable("userId") Integer userId, @PathVariable("name") String name, Authentication authentication) {
        return ResponseEntity.ok(modelMapper.map(userCompetitionService.removeParticipationByName(userId, name, authentication.getName()), UserDto.class));
    }

    @PutMapping("/remove/byId/{compId}")
    public ResponseEntity<UserDto> removeParticipationById(@PathVariable("compId") Integer compId, Authentication authentication) {
        return removeParticipationById(userCompetitionService.findAuthenticatedUser(authentication.getName()).getUserId(), compId, authentication);
    }

    @PutMapping("/remove/byName/{name}")
    public ResponseEntity<UserDto> removeParticipationByName(@PathVariable("name") String name, Authentication authentication) {
        return removeParticipationByName(userCompetitionService.findAuthenticatedUser(authentication.getName()).getUserId(), name, authentication);
    }
}
