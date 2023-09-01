package com.orienting.common.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Getter;
import lombok.NonNull;

import java.util.Date;

@Data
@Getter
public class UserDto {
    private Integer userId;
    @Email
    private String email;
    private String name;
    private String surname;
    private String ucn;
    @Pattern(regexp = "^(\\+\\d{1,2})?\\s?\\(?\\d{3}\\)?[-.\\s]?\\d{3}[-.\\s]?\\d{4}$", message = "Invalid phone number")
    private String phoneNumber;
    @NotNull
    private Integer clubId;
    @Pattern(regexp = "^(W|M)\\d{2}$", message = "Invalid format")
    private String group;
    @Pattern(regexp = "^(?i)(coach|competitor)$", message = "Invalid role")
    private String role;

    public UserDto() {}

}
