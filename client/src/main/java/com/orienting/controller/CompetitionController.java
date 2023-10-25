package com.orienting.controller;

import com.orienting.common.dto.*;

import java.util.ArrayList;
import java.util.List;

public class CompetitionController extends MainController{
    public CompetitionController(String url) {
        super(url);
    }
    public List<CompetitionDto> getAllCompetitions() {
        return (List<CompetitionDto>) makeRequest(null, url + "/all", "GET", new ArrayList<CompetitionDto>());
    }

    public List<CompetitionRequestDto> getAllCompetitionsWithParticipants() {
        return (List<CompetitionRequestDto>) makeRequest(null, url + "/allWithParticipants", "GET", new ArrayList<CompetitionRequestDto>());
    }

    public List<CompetitionDto> getCompetitionByDate(String date) {
        return (List<CompetitionDto>) makeRequest(null, url + "/byDate/" + date, "GET", new ArrayList<CompetitionDto>());
    }
    public List<CompetitionRequestDto> getCompetitionByDateWithParticipants(String date) {
        return (List<CompetitionRequestDto>) makeRequest(null, url + "/withUsers/byDate/" + date, "GET", new ArrayList<CompetitionRequestDto>());
    }

    public CompetitionDto getCompetitionByName(String name) {
        return (CompetitionDto) makeRequest(null, url + "/byName/" + name, "GET", new CompetitionDto());
    }
    public CompetitionRequestDto getCompetitionByNameWithParticipants(String name) {
        return (CompetitionRequestDto) makeRequest(null, url + "/withUsers/byName/" + name, "GET", new CompetitionRequestDto());
    }

    public CompetitionDto getCompetitionById(Integer id) {
        return (CompetitionDto) makeRequest(null, url + "/byCompId/" + id, "GET", new CompetitionDto());
    }
    public CompetitionRequestDto getCompetitionByIdWithParticipants(Integer id) {
        return (CompetitionRequestDto) makeRequest(null, url + "/withUsers/byCompId/" + id, "GET", new CompetitionRequestDto());
    }

    public CompetitionDto createCompetition(CompetitionDto competition) {
        return (CompetitionDto) makeRequest(competition, url + "/add", "POST", new CompetitionDto());
    }

    public CompetitionDto deleteCompetitionById(Integer id) {
        return (CompetitionDto) makeRequest(null, url + "/delete/byCompId/" + id, "DELETE", new CompetitionDto());
    }
    public CompetitionDto deleteCompetitionByName(String name) {
        return (CompetitionDto) makeRequest(null, url + "/delete/byName/" + name, "DELETE", new CompetitionDto());
    }
    public CompetitionDto updateCompetitionByCompId(Integer id, CompetitionUpdateDto competition) {
        return (CompetitionDto) makeRequest(competition, url + "/update/byCompId/" + id, "PUT", new CompetitionDto());
    }
    public CompetitionDto updateCompetitionByName(String name, CompetitionUpdateDto competition) {
        return (CompetitionDto) makeRequest(competition, url + "/update/byName/" + name, "PUT", new CompetitionDto());
    }

    public UserDto requestParticipationById(Integer userId, Integer compId) {
        return (UserDto) makeRequest(null, url + "/request/byId/" + userId + "/" + compId, "PUT", new UserDto());
    }
    public UserDto requestParticipationByName(Integer userId, String compName) {
        return (UserDto) makeRequest(null, url + "/request/byName/" + userId + "/" + compName, "PUT", new UserDto());
    }

    public UserDto requestLoggedInUserParticipationById(Integer compId) {
        return (UserDto) makeRequest(null, url + "/request/byId/" + compId, "PUT", new UserDto());
    }
    public UserDto requestLoggedInUserParticipationByName(String compName) {
        return (UserDto) makeRequest(null, url + "/request/byName/" + compName, "PUT", new UserDto());
    }

    public UserDto removeParticipationById(Integer userId, Integer compId) {
        return (UserDto) makeRequest(null, url + "/remove/byId/" + userId + "/" + compId, "PUT", new UserDto());
    }
    public UserDto removeParticipationByName(Integer userId, String compName) {
        return (UserDto) makeRequest(null, url + "/remove/byName/" + userId + "/" + compName, "PUT", new UserDto());
    }

    public UserDto removeLoggedInUserParticipationById(Integer compId) {
        return (UserDto) makeRequest(null, url + "/remove/byId/" + compId, "PUT", new UserDto());
    }
    public UserDto removeLoggedInUserParticipationByName(String compName) {
        return (UserDto) makeRequest(null, url + "/remove/byName/" + compName, "PUT", new UserDto());
    }
}
