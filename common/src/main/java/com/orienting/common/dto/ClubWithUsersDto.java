package com.orienting.common.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class ClubWithUsersDto {
    private Integer clubId;
    private String clubName;
    private String city;
    private Set<UserDto> competitors;
    private Set<CoachDto> coaches;

    public ClubWithUsersDto() {}
}


