package com.orienting.common.dto;

import lombok.Data;
import lombok.Getter;


@Data
@Getter
public class ClubDto {

    private String clubName;
    private String city;
    private Integer coachId;

    public ClubDto() {}
}


