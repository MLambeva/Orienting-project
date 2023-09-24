package com.orienting.common.dto;

import com.orienting.common.entity.UserRole;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
    private Integer userId;
    //@Email(message = "Invalid email!")
    private String email;
    private String firstName;
    private String lastName;
    private String ucn;
    //@Pattern(regexp = "^(\\+\\d{1,2})?\\s?\\(?\\d{3}\\)?[-.\\s]?\\d{3}[-.\\s]?\\d{4}$", message = "Invalid phone number")
    private String phoneNumber;
    //@Pattern(regexp = "^(W|M)\\d{2}$", message = "Invalid format")
    private String group;
    //@Pattern(regexp = "^(?i)(coach|competitor)$", message = "Invalid role")
    private UserRole role;
    private Integer clubId;
    private String clubName;
    private String city;

    public UserDto() {}

}
