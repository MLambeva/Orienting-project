package com.orienting.common.dto;

import com.orienting.common.enums.UserRole;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class UsersWithRequestedCompetitionsDto {
    private Integer userId;
    private String email;
    private String firstName;
    private String lastName;
    private String group;
    private UserRole role;
    private String clubName;
    private String city;
    private Set<CompetitionDto> competitions;
}
