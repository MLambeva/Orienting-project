package com.orienting.common.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ClubWithUsersDto {
    private Integer clubId;
    private String clubName;
    private String city;
    private Set<CompetitorsAndCoachDto> competitors;
    private Set<CompetitorsAndCoachDto> coaches;
}


