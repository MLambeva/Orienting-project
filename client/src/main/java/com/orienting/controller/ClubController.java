package com.orienting.controller;

import com.orienting.common.dto.ClubDto;
import com.orienting.common.dto.ClubWithUsersDto;

import java.util.ArrayList;
import java.util.List;

public class ClubController extends MainController {
    public ClubController(String url) {
        super(url);
    }
    public List<ClubDto> getAllClubs() {
        return (List<ClubDto>) makeRequest(null, url + "/all", "GET", new ArrayList<ClubDto>());
    }

    public List<ClubWithUsersDto> getAllClubsWithUsers() {
        return (List<ClubWithUsersDto>) makeRequest(null, url + "/allWithUsers", "GET", new ArrayList<ClubWithUsersDto>());
    }

    public ClubDto getClubById(Integer id) {
        return (ClubDto) makeRequest(null, url + "/byId/" + id, "GET", new ClubDto());
    }

    public ClubDto getClubByName(String name) {
        return (ClubDto) makeRequest(null, url + "/byName/" + name, "GET", new ClubDto());
    }

    public ClubWithUsersDto getClubWithUsersById(Integer id) {
        return (ClubWithUsersDto) makeRequest(null, url + "/withUsers/byId/" + id, "GET", new ClubWithUsersDto());
    }

    public ClubWithUsersDto getClubWithUsersByName(String name) {
        return (ClubWithUsersDto) makeRequest(null, url + "/withUsers/byName/" + name, "GET", new ClubWithUsersDto());
    }

    public ClubDto addClub(ClubDto clubDto) {
        return (ClubDto) makeRequest(clubDto, url + "/add", "POST", new ClubDto());
    }

    public ClubDto deleteClubById(Integer id) {
        return (ClubDto) makeRequest(null, url + "/delete/byId/" + id, "DELETE", new ClubDto());
    }

    public ClubDto deleteClubByName(String name) {
        return (ClubDto) makeRequest(null, url + "/delete/byName/" + name, "DELETE", new ClubDto());
    }

    public ClubDto updateClubById(Integer id, ClubDto club) {
        return (ClubDto) makeRequest(club, url + "/update/byId/" + id, "PUT", new ClubDto());
    }

    public ClubDto updateClubByName(String name, ClubDto club) {
        return (ClubDto) makeRequest(club, url + "/update/byName/" + name, "PUT", new ClubDto());
    }
}
