package com.orienting.controller;

import com.orienting.common.dto.*;

import java.util.ArrayList;
import java.util.List;

public class CompetitionController extends MainController{
    private final String COMP_URL = "http://localhost:8080/api/competitions";

    public List<CompetitionDto> getAllCompetitions() {
        return (List<CompetitionDto>) makeRequest(null, COMP_URL + "/all", "GET", new ArrayList<CompetitionDto>());
    }

    public List<CompetitionRequestDto> getAllCompetitionsWithParticipants() {
        return (List<CompetitionRequestDto>) makeRequest(null, COMP_URL + "/allWithParticipants", "GET", new ArrayList<CompetitionRequestDto>());
    }

    public List<CompetitionDto> getCompetitionByDate(String date) {
        return (List<CompetitionDto>) makeRequest(null, COMP_URL + "/byDate/" + date, "GET", new ArrayList<CompetitionDto>());
    }
    public List<CompetitionRequestDto> getCompetitionByDateWithParticipants(String date) {
        return (List<CompetitionRequestDto>) makeRequest(null, COMP_URL + "/withUsers/byDate/" + date, "GET", new ArrayList<CompetitionRequestDto>());
    }

    public CompetitionDto getCompetitionByName(String name) {
        return (CompetitionDto) makeRequest(null, COMP_URL + "/byName/" + name, "GET", new CompetitionDto());
    }
    public CompetitionRequestDto getCompetitionByNameWithParticipants(String name) {
        return (CompetitionRequestDto) makeRequest(null, COMP_URL + "/withUsers/byName/" + name, "GET", new CompetitionRequestDto());
    }

    public CompetitionDto getCompetitionById(Integer id) {
        return (CompetitionDto) makeRequest(null, COMP_URL + "/byCompId/" + id, "GET", new CompetitionDto());
    }
    public CompetitionRequestDto getCompetitionByIdWithParticipants(Integer id) {
        return (CompetitionRequestDto) makeRequest(null, COMP_URL + "/withUsers/byCompId/" + id, "GET", new CompetitionRequestDto());
    }

    public CompetitionDto createCompetition(CompetitionDto competition) {
        return (CompetitionDto) makeRequest(competition, COMP_URL + "/add", "POST", new CompetitionDto());
    }

    public CompetitionDto deleteCompetitionById(Integer id) {
        return (CompetitionDto) makeRequest(null, COMP_URL + "/delete/byCompId/" + id, "DELETE", new CompetitionDto());
    }
    public CompetitionDto deleteCompetitionByName(String name) {
        return (CompetitionDto) makeRequest(null, COMP_URL + "/delete/byName/" + name, "DELETE", new CompetitionDto());
    }
    public CompetitionDto updateCompetitionByCompId(Integer id, CompetitionUpdateDto competition) {
        return (CompetitionDto) makeRequest(competition, COMP_URL + "/update/byCompId/" + id, "PUT", new CompetitionDto());
    }
    public CompetitionDto updateCompetitionByName(String name, CompetitionUpdateDto competition) {
        return (CompetitionDto) makeRequest(competition, COMP_URL + "/update/byName/" + name, "PUT", new CompetitionDto());
    }

    public UserDto requestParticipationById(Integer userId, Integer compId) {
        return (UserDto) makeRequest(null, COMP_URL + "/request/byId/" + userId + "/" + compId, "PUT", new UserDto());
    }
    public UserDto requestParticipationByName(Integer userId, String compName) {
        return (UserDto) makeRequest(null, COMP_URL + "/request/byName/" + userId + "/" + compName, "PUT", new UserDto());
    }

    public UserDto requestLoggedInUserParticipationById(Integer compId) {
        return (UserDto) makeRequest(null, COMP_URL + "/request/byId/" + compId, "PUT", new UserDto());
    }
    public UserDto requestLoggedInUserParticipationByName(String compName) {
        return (UserDto) makeRequest(null, COMP_URL + "/request/byName/" + compName, "PUT", new UserDto());
    }

    public UserDto removeParticipationById(Integer userId, Integer compId) {
        return (UserDto) makeRequest(null, COMP_URL + "/remove/byId/" + userId + "/" + compId, "PUT", new UserDto());
    }
    public UserDto removeParticipationByName(Integer userId, String compName) {
        return (UserDto) makeRequest(null, COMP_URL + "/remove/byName/" + userId + "/" + compName, "PUT", new UserDto());
    }

    public UserDto removeLoggedInUserParticipationById(Integer compId) {
        return (UserDto) makeRequest(null, COMP_URL + "/remove/byId/" + compId, "PUT", new UserDto());
    }
    public UserDto removeLoggedInUserParticipationByName(String compName) {
        return (UserDto) makeRequest(null, COMP_URL + "/remove/byName/" + compName, "PUT", new UserDto());
    }

}
