package com.orienting.common.dto;

import com.orienting.common.entity.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserUpdateDto {
    @Email(message = "Invalid email!")
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    @Pattern(regexp = "^(\\+\\d{1,2})?\\s?\\(?\\d{3}\\)?[-.\\s]?\\d{3}[-.\\s]?\\d{4}$", message = "Invalid phone number")
    private String phoneNumber;
    @Pattern(regexp = "^(W|M)\\d{2}$", message = "Invalid format")
    private String group;
    @Pattern(regexp = "^(?i)(coach|competitor)$", message = "Invalid role")
    private UserRole role;
    private Integer clubId;
}
