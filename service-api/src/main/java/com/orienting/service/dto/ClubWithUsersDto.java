package com.orienting.service.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class ClubWithUsersDto {
    private Integer clubId;
    private String clubName;
    private String city;
    private Set<CompetitorsAndCoachDto> competitors;
    private Set<CompetitorsAndCoachDto> coaches;
}


