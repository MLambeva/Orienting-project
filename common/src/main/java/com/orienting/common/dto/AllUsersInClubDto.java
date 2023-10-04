package com.orienting.common.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;


@Getter
@Setter
public class AllUsersInClubDto {
    private Set<CompetitorsAndCoachDto> competitors;
    private Set<CompetitorsAndCoachDto> coaches;
}
