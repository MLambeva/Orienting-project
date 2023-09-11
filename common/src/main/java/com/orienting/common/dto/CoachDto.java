package com.orienting.common.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CoachDto {
    private Integer userId;
    @Email
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private Integer clubId;
    private String clubName;
    private String city;
}
