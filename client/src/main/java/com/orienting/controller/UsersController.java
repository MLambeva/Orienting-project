package com.orienting.controller;

import com.orienting.common.dto.*;

import java.util.ArrayList;
import java.util.List;

public class UsersController extends MainController {

    public UsersController(String url) {
        super(url);
    }

    public UserDto getLoggedInUser() {
        return (UserDto) makeRequest(null, url + "/me", "GET", new UserDto());
    }

    public List<UserDto> getAllUsers() {
        return (List<UserDto>) makeRequest(null, url + "/all", "GET", new ArrayList<UserDto>());
    }

    public UserDto getUserById(Integer userId) {
        return (UserDto) makeRequest(null, url + "/byId/" + userId, "GET", new UserDto());
    }

    public UserDto getUserByUcn(String ucn) {
        return (UserDto) makeRequest(null, url + "/byUcn/" + ucn, "GET", new UserDto());
    }

    public String getLoggedInUserRole() {
        return (String) makeRequest(null, url + "/role", "GET", new String());
    }

    public String getRoleById(Integer id) {
        return (String) makeRequest(null, url + "/role/byId/" + id, "GET", new String());
    }

    public String getRoleByUcn(String ucn) {
        return (String) makeRequest(null, url + "/role/byUcn/" + ucn, "GET", new String());
    }

    public List<CompetitorDto> getAllCoaches() {
        return (List<CompetitorDto>) makeRequest(null, url + "/allCoaches", "GET", new ArrayList<CompetitorDto>());
    }

    public List<CompetitorDto> getAllCompetitors() {
        return (List<CompetitorDto>) makeRequest(null, url + "/allCompetitors", "GET", new ArrayList<CompetitorDto>());
    }

    public CompetitorsWithCoachesDto getUserWithCoachesById(Integer id) {
        return (CompetitorsWithCoachesDto) makeRequest(null, url + "/withCoaches/" + id, "GET", new CompetitorsWithCoachesDto());
    }

    public CompetitorsWithCoachesDto getLoggedInUserWithCoaches() {
        return (CompetitorsWithCoachesDto) makeRequest(null, url + "/withCoaches", "GET", new CompetitorsWithCoachesDto());
    }

    public UsersWithRequestedCompetitionsDto getUserWithRequestedCompetitionsById(Integer id) {
        return (UsersWithRequestedCompetitionsDto) makeRequest(null, url + "/competition/" + id, "GET", new UsersWithRequestedCompetitionsDto());
    }

    public UsersWithRequestedCompetitionsDto getLoggedInUserWithRequestedCompetitions() {
        return (UsersWithRequestedCompetitionsDto) makeRequest(null, url + "/competition", "GET", new UsersWithRequestedCompetitionsDto());
    }

    public List<CompetitorsAndCoachDto> getAllUsersInClubById(Integer clubId) {
        return (List<CompetitorsAndCoachDto>) makeRequest(null, url + "/allUsersInClub/byId/" + clubId, "GET", new ArrayList<CompetitorsAndCoachDto>());
    }

    public List<CompetitorsAndCoachDto> getAllCompetitorsInClubById(Integer clubId) {
        return (List<CompetitorsAndCoachDto>) makeRequest(null, url + "/allCompetitorsInClub/byId/" + clubId, "GET", new ArrayList<CompetitorsAndCoachDto>());
    }

    public List<CompetitorsAndCoachDto> getAllCoachesInClubById(Integer clubId) {
        return (List<CompetitorsAndCoachDto>) makeRequest(null, url + "/allCoachesInClub/byId/" + clubId, "GET", new ArrayList<CompetitorsAndCoachDto>());
    }

    public List<CompetitorsAndCoachDto> getAllUsersInClubByName(String clubName) {
        return (List<CompetitorsAndCoachDto>) makeRequest(null, url + "/allUsersInClub/byName/" + clubName, "GET", new ArrayList<CompetitorsAndCoachDto>());
    }

    public List<CompetitorsAndCoachDto> getAllCompetitorsInClubByName(String clubName) {
        return (List<CompetitorsAndCoachDto>) makeRequest(null, url + "/allCompetitorsInClub/byName/" + clubName, "GET", new ArrayList<CompetitorsAndCoachDto>());
    }

    public List<CompetitorsAndCoachDto> getAllCoachesInClubByName(String clubName) {
        return (List<CompetitorsAndCoachDto>) makeRequest(null, url + "/allCoachesInClub/byName/" + clubName, "GET", new ArrayList<CompetitorsAndCoachDto>());
    }

    public AllUsersInClubDto getAllUsersInClub() {
        return (AllUsersInClubDto) makeRequest(null, url + "/competitorsAndCoaches", "GET", new AllUsersInClubDto());
    }

    public List<CompetitorsAndCoachDto> getAllCoachesInClub() {
        return (List<CompetitorsAndCoachDto>) makeRequest(null, url + "/coaches", "GET", new ArrayList<AllUsersInClubDto>());
    }

    public List<CompetitorsAndCoachDto> getAllCompetitorsInClub() {
        return (List<CompetitorsAndCoachDto>) makeRequest(null, url + "/competitors", "GET", new ArrayList<AllUsersInClubDto>());
    }

    public ClubDto getLoginUserClub() {
        return (ClubDto) makeRequest(null, url + "/club", "GET", new ClubDto());
    }

    public UserDto deleteUserByUserId(Integer id) {
        return (UserDto) makeRequest(null, url + "/remove/byId/" + id, "DELETE", new UserDto());
    }

    public UserDto deleteUserByUcn(String ucn) {
        return (UserDto) makeRequest(null, url + "/remove/byUcn/" + ucn, "DELETE", new UserDto());
    }

    public UserDto leftClubById(Integer id) {
        return (UserDto) makeRequest(null, url + "/leftClub/" + id, "PUT", new UserDto());
    }

    public UserDto leftClubLoggedInUser() {
        return (UserDto) makeRequest(null, url + "/leftClub", "PUT", new UserDto());
    }

    public UserDto makeCoach(Integer id) {
        return (UserDto) makeRequest(null, url + "/makeCoach/" + id, "PUT", new UserDto());
    }

    public UserDto removeCoach(Integer id) {
        return (UserDto) makeRequest(null, url + "/removeCoach/" + id, "PUT", new UserDto());
    }

    public UserDto setCoachToClub(Integer userId, Integer clubId) {
        return (UserDto) makeRequest(null, url + "/setCoach/" + userId + "/" + clubId, "PUT", new UserDto());
    }

    public UserDto addClubToUser(Integer userId, Integer clubId) {
        return (UserDto) makeRequest(null, url + "/addClub/" + userId + "/" + clubId, "PUT", new UserDto());
    }

    public UserDto addClubToLoggedInUser(Integer clubId) {
        return (UserDto) makeRequest(null, url + "/addClub/" + clubId, "PUT", new UserDto());
    }

    public UserDto updateUserByUserId(Integer userId, UserUpdateDto user) {
        return (UserDto) makeRequest(user, url + "/update/byUserId/" + userId, "PUT", new UserDto());
    }

    public UserDto updateUserByUcn(String ucn, UserUpdateDto user) {
        return (UserDto) makeRequest(user, url + "/update/byUcn/" + ucn, "PUT", new UserDto());
    }

    public UserDto updateLoggedInUser(UserUpdateDto user) {
        return (UserDto) makeRequest(user, url + "/update", "PUT", new UserDto());
    }

    public UserDto addUser(UserCreationDto user) {
        return (UserDto) makeRequest(user, url + "/addUser", "POST", new UserDto());
    }
}

