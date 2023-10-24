package com.orienting.controller;

import com.orienting.common.dto.*;

import java.util.ArrayList;
import java.util.List;

public class UsersController extends MainController {
    private final String USER_URL = "http://localhost:8080/api/users";

    public UserDto getLoggedInUser() {
        return (UserDto) makeRequest(null, USER_URL + "/me", "GET", new UserDto());
    }

    public List<UserDto> getAllUsers() {
        return (List<UserDto>) makeRequest(null, USER_URL + "/all", "GET", new ArrayList<UserDto>());
    }

    public UserDto getUserById(Integer userId) {
        return (UserDto) makeRequest(null, USER_URL + "/byId/" + userId, "GET", new UserDto());
    }

    public UserDto getUserByUcn(String ucn) {
        return (UserDto) makeRequest(null, USER_URL + "/byUcn/" + ucn, "GET", new UserDto());
    }

    public String getLoggedInUserRole() {
        return (String) makeRequest(null, USER_URL + "/role", "GET", new String());
    }

    public String getRoleById(Integer id) {
        return (String) makeRequest(null, USER_URL + "/role/byId/" + id, "GET", new String());
    }

    public String getRoleByUcn(String ucn) {
        return (String) makeRequest(null, USER_URL + "/role/byUcn/" + ucn, "GET", new String());
    }

    public List<CompetitorDto> getAllCoaches() {
        return (List<CompetitorDto>) makeRequest(null, USER_URL + "/allCoaches", "GET", new ArrayList<CompetitorDto>());
    }

    public List<CompetitorDto> getAllCompetitors() {
        return (List<CompetitorDto>) makeRequest(null, USER_URL + "/allCompetitors", "GET", new ArrayList<CompetitorDto>());
    }

    public CompetitorsWithCoachesDto getUserWithCoachesById(Integer id) {
        return (CompetitorsWithCoachesDto) makeRequest(null, USER_URL + "/withCoaches/" + id, "GET", new CompetitorsWithCoachesDto());
    }

    public CompetitorsWithCoachesDto getLoggedInUserWithCoaches() {
        return (CompetitorsWithCoachesDto) makeRequest(null, USER_URL + "/withCoaches", "GET", new CompetitorsWithCoachesDto());
    }

    public UsersWithRequestedCompetitionsDto getUserWithRequestedCompetitionsById(Integer id) {
        return (UsersWithRequestedCompetitionsDto) makeRequest(null, USER_URL + "/competition/" + id, "GET", new UsersWithRequestedCompetitionsDto());
    }

    public UsersWithRequestedCompetitionsDto getLoggedInUserWithRequestedCompetitions() {
        return (UsersWithRequestedCompetitionsDto) makeRequest(null, USER_URL + "/competition", "GET", new UsersWithRequestedCompetitionsDto());
    }

    public List<CompetitorsAndCoachDto> getAllUsersInClubById(Integer clubId) {
        return (List<CompetitorsAndCoachDto>) makeRequest(null, USER_URL + "/allUsersInClub/byId/" + clubId, "GET", new ArrayList<CompetitorsAndCoachDto>());
    }

    public List<CompetitorsAndCoachDto> getAllCompetitorsInClubById(Integer clubId) {
        return (List<CompetitorsAndCoachDto>) makeRequest(null, USER_URL + "/allCompetitorsInClub/byId/" + clubId, "GET", new ArrayList<CompetitorsAndCoachDto>());
    }

    public List<CompetitorsAndCoachDto> getAllCoachesInClubById(Integer clubId) {
        return (List<CompetitorsAndCoachDto>) makeRequest(null, USER_URL + "/allCoachesInClub/byId/" + clubId, "GET", new ArrayList<CompetitorsAndCoachDto>());
    }

    public List<CompetitorsAndCoachDto> getAllUsersInClubByName(String clubName) {
        return (List<CompetitorsAndCoachDto>) makeRequest(null, USER_URL + "/allUsersInClub/byName/" + clubName, "GET", new ArrayList<CompetitorsAndCoachDto>());
    }

    public List<CompetitorsAndCoachDto> getAllCompetitorsInClubByName(String clubName) {
        return (List<CompetitorsAndCoachDto>) makeRequest(null, USER_URL + "/allCompetitorsInClub/byName/" + clubName, "GET", new ArrayList<CompetitorsAndCoachDto>());
    }

    public List<CompetitorsAndCoachDto> getAllCoachesInClubByName(String clubName) {
        return (List<CompetitorsAndCoachDto>) makeRequest(null, USER_URL + "/allCoachesInClub/byName/" + clubName, "GET", new ArrayList<CompetitorsAndCoachDto>());
    }

    public AllUsersInClubDto getAllUsersInClub() {
        return (AllUsersInClubDto) makeRequest(null, USER_URL + "/competitorsAndCoaches", "GET", new AllUsersInClubDto());
    }

    public List<CompetitorsAndCoachDto> getAllCoachesInClub() {
        return (List<CompetitorsAndCoachDto>) makeRequest(null, USER_URL + "/coaches", "GET", new ArrayList<AllUsersInClubDto>());
    }

    public List<CompetitorsAndCoachDto> getAllCompetitorsInClub() {
        return (List<CompetitorsAndCoachDto>) makeRequest(null, USER_URL + "/competitors", "GET", new ArrayList<AllUsersInClubDto>());
    }

    public ClubDto getLoginUserClub() {
        return (ClubDto) makeRequest(null, USER_URL + "/club", "GET", new ClubDto());
    }

    public UserDto deleteUserByUserId(Integer id) {
        return (UserDto) makeRequest(null, USER_URL + "/remove/byId/" + id, "DELETE", new UserDto());
    }

    public UserDto deleteUserByUcn(String ucn) {
        return (UserDto) makeRequest(null, USER_URL + "/remove/byUcn/" + ucn, "DELETE", new UserDto());
    }

    public UserDto leftClubById(Integer id) {
        return (UserDto) makeRequest(null, USER_URL + "/leftClub/" + id, "PUT", new UserDto());
    }

    public UserDto leftClubLoggedInUser() {
        return (UserDto) makeRequest(null, USER_URL + "/leftClub", "PUT", new UserDto());
    }

    public UserDto makeCoach(Integer id) {
        return (UserDto) makeRequest(null, USER_URL + "/makeCoach/" + id, "PUT", new UserDto());
    }

    public UserDto removeCoach(Integer id) {
        return (UserDto) makeRequest(null, USER_URL + "/removeCoach/" + id, "PUT", new UserDto());
    }

    public UserDto setCoachToClub(Integer userId, Integer clubId) {
        return (UserDto) makeRequest(null, USER_URL + "/setCoach/" + userId + "/" + clubId, "PUT", new UserDto());
    }

    public UserDto addClubToUser(Integer userId, Integer clubId) {
        return (UserDto) makeRequest(null, USER_URL + "/addClub/" + userId + "/" + clubId, "PUT", new UserDto());
    }

    public UserDto addClubToLoggedInUser(Integer clubId) {
        return (UserDto) makeRequest(null, USER_URL + "/addClub/" + clubId, "PUT", new UserDto());
    }

    public UserDto updateUserByUserId(Integer userId, UserUpdateDto user) {
        return (UserDto) makeRequest(user, USER_URL + "/update/byUserId/" + userId, "PUT", new UserDto());
    }

    public UserDto updateUserByUcn(String ucn, UserUpdateDto user) {
        return (UserDto) makeRequest(user, USER_URL + "/update/byUcn/" + ucn, "PUT", new UserDto());
    }

    public UserDto updateLoggedInUser(UserUpdateDto user) {
        return (UserDto) makeRequest(user, USER_URL + "/update", "PUT", new UserDto());
    }

    public UserDto addUser(UserCreationDto user) {
        return (UserDto) makeRequest(user, USER_URL + "/addUser", "POST", new UserDto());
    }
}

