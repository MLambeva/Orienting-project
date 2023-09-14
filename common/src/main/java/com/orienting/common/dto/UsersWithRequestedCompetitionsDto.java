package com.orienting.common.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class UsersWithRequestedCompetitionsDto {
    private Integer userId;
    private String email;
    private String firstName;
    private String lastName;
    private String group;
    private String role;
    private String clubName;
    private String city;
    private Set<CompetitionDto> competitions;

    UsersWithRequestedCompetitionsDto() {}
}
