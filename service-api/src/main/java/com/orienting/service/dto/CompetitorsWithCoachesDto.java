package com.orienting.service.dto;

import com.orienting.service.entity.UserRole;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class CompetitorsWithCoachesDto {
    private Integer userId;
    private String email;
    private String firstName;
    private String lastName;
    private String ucn;
    private String phoneNumber;
    private String group;
    private UserRole role;
    private Integer clubId;
    private String clubName;
    private String city;
    private Set<CompetitorsAndCoachDto> coaches;
}
