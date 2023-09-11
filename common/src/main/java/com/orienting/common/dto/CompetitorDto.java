package com.orienting.common.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompetitorDto {
    private String firstName;
    private String lastName;
    private String group;
    private String clubName;
    private String city;

    public CompetitorDto() {}
}
