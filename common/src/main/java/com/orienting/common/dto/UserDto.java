package com.orienting.common.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UserDto {
    private Integer userId;
    @Email
    private String email;
    private String firstName;
    private String lastName;
    private String ucn;
    //@Pattern(regexp = "^(\\+\\d{1,2})?\\s?\\(?\\d{3}\\)?[-.\\s]?\\d{3}[-.\\s]?\\d{4}$", message = "Invalid phone number")
    private String phoneNumber;
    //@Pattern(regexp = "^(W|M)\\d{2}$", message = "Invalid format")
    private String group;
    //@Pattern(regexp = "^(?i)(coach|competitor)$", message = "Invalid role")
    private String role;
    private Integer clubId;
    private String clubName;
    private String city;

    public UserDto() {}

}
