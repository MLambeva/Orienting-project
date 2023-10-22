package com.orienting.controller;

import com.orienting.Main;
import com.orienting.common.dto.ClubDto;
import com.orienting.common.dto.ClubWithUsersDto;

import java.util.ArrayList;
import java.util.List;

public class ClubController extends MainController {
    private final String CLUB_URL = "http://localhost:8080/api/clubs";

    public List<ClubDto> getAllClubs() {
        return (List<ClubDto>) makeRequest(null, CLUB_URL + "/all", "GET", new ArrayList<ClubDto>());
    }

    public List<ClubWithUsersDto> getAllClubsWithUsers() {
        return (List<ClubWithUsersDto>) makeRequest(null, CLUB_URL + "/allWithUsers", "GET", new ArrayList<ClubWithUsersDto>());
    }

    public ClubDto getClubById(Integer id) {
        return (ClubDto) makeRequest(null, CLUB_URL + "/byId/" + id, "GET", new ClubDto());
    }

    public ClubDto getClubByName(String name) {
        return (ClubDto) makeRequest(null, CLUB_URL + "/byName/" + name, "GET", new ClubDto());
    }

    public ClubWithUsersDto getClubWithUsersById(Integer id) {
        return (ClubWithUsersDto) makeRequest(null, CLUB_URL + "/withUsers/byId/" + id, "GET", new ClubWithUsersDto());
    }

    public ClubWithUsersDto getClubWithUsersByName(String name) {
        return (ClubWithUsersDto) makeRequest(null, CLUB_URL + "/withUsers/byName/" + name, "GET", new ClubWithUsersDto());
    }

    public ClubDto addClub(ClubDto clubDto) {
        return (ClubDto) makeRequest(clubDto, CLUB_URL + "/add", "POST", new ClubDto());
    }

    public ClubDto deleteClubById(Integer id) {
        return (ClubDto) makeRequest(null, CLUB_URL + "/delete/byId/" + id, "DELETE", new ClubDto());
    }

    public ClubDto deleteClubByName(String name) {
        return (ClubDto) makeRequest(null, CLUB_URL + "/delete/byName/" + name, "DELETE", new ClubDto());
    }

    public ClubDto updateClubById(Integer id, ClubDto club) {
        return (ClubDto) makeRequest(club, CLUB_URL + "/update/byId/" + id, "PUT", new ClubDto());
    }

    public ClubDto updateClubByName(String name, ClubDto club) {
        return (ClubDto) makeRequest(club, CLUB_URL + "/update/byName/" + name, "PUT", new ClubDto());
    }
}
