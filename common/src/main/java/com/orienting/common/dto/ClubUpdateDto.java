package com.orienting.common.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClubUpdateDto {
    private Integer clubId;
    private String clubName;
    private String city;

    public ClubUpdateDto() {}
}
