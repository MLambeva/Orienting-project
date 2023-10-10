package com.orienting.service.dto;

import com.orienting.service.entity.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCreationDto {
    @NotBlank(message = "The email is mandatory!")
    @Email(message = "Invalid email!")
    private String email;
    @NotBlank(message = "The password is mandatory!")
    private String password;
    @NotBlank(message = "First name is mandatory!")
    private String firstName;
    @NotBlank(message = "Last name is mandatory!")
    private String lastName;
    @NotBlank(message = "Unified Civil number is mandatory!")
    private String ucn;
    @Pattern(regexp = "^(\\+\\d{1,2})?\\s?\\(?\\d{3}\\)?[-.\\s]?\\d{3}[-.\\s]?\\d{4}$", message = "Invalid phone number")
    private String phoneNumber;
    @Pattern(regexp = "^(W|M)\\d{2}$", message = "Invalid format")
    private String group;
    @NotNull(message = "The role is mandatory!")
    private UserRole role;
    private Integer clubId;
    private String clubName;

    public UserCreationDto(String email, String password, String firstName, String lastName, String ucn, String phoneNumber, String group, UserRole role, Integer clubId) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.ucn = ucn;
        this.phoneNumber = phoneNumber;
        this.group = group;
        this.role = role;
        if(clubId != null) this.clubId = clubId;
    }


}
