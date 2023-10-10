package com.orienting.service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClubDto {
    private Integer clubId;
    @NotBlank(message = "Club name is mandatory!")
    private String clubName;
    @NotBlank(message = "Club city is mandatory!")
    private String city;

    public ClubDto() {}

}
