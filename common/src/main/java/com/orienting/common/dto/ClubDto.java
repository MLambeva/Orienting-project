package com.orienting.common.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Data
@Getter
@Setter
public class ClubDto {
    private Integer clubId;
    private String clubName;
    private String city;
    private Integer coachId;

    public ClubDto() {}
}


