package com.orienting.common.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class CompetitorsWithCoachesDto {
    private Integer userId;
    private String email;
    private String firstName;
    private String lastName;
    private String ucn;
    private String phoneNumber;
    private String group;
    private String role;
    private Integer clubId;
    private String clubName;
    private String city;
    private Set<CompetitorsAndCoachDto> coaches;

    public CompetitorsWithCoachesDto() {}
}
