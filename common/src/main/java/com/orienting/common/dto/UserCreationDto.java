package com.orienting.common.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.orienting.common.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).{8,}$", message = "Invalid password!")
    private String password;
    @NotBlank(message = "First name is mandatory!")
    private String firstName;
    @NotBlank(message = "Last name is mandatory!")
    private String lastName;
    @Pattern(regexp = "^\\d{10}$", message = "UCN must have 10 digits")
    @NotBlank(message = "Unified Civil number is mandatory!")
    private String ucn;
    @Pattern(regexp = "^(\\+\\d{1,2})?\\s?\\(?\\d{3}\\)?[-.\\s]?\\d{3}[-.\\s]?\\d{4}$", message = "Invalid phone number!")
    private String phoneNumber;
    @Pattern(regexp = "^(W|M)\\d{2}$", message = "Invalid format! Format must be in format M/W(dd), d-digit --> for example: M21")
    private String group;
    @NotNull(message = "The role is mandatory!")
    private UserRole role;
    private Integer clubId;
    private String clubName;
    @JsonIgnore
    private Logger logger = LoggerFactory.getLogger(UserCreationDto.class);


    public UserCreationDto(String email, String password, String firstName, String lastName, String ucn, String phoneNumber, String group, UserRole role, Integer clubId) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.ucn = ucn;
        this.phoneNumber = phoneNumber;
        this.group = group;
        this.role = role;
        if (clubId != null) this.clubId = clubId;
    }

    public UserCreationDto(String email, String password, String firstName, String lastName, String ucn, String phoneNumber, String group, String role, String clubId, String clubName) {
        if (email != null && !email.isEmpty()) this.email = email;
        else {
            logger.error("The email is mandatory!");
            this.email = null;
        }

        if (password != null && !password.isEmpty()) this.password = password;
        else {
            logger.error("The password is mandatory!");
            this.password = null;
        }
        if (firstName != null && !firstName.isEmpty()) this.firstName = firstName;
        else {
            logger.error("First name is mandatory!");
            this.firstName = null;
        }
        if (lastName != null && !lastName.isEmpty()) this.lastName = firstName;
        else {
            logger.error("Last name is mandatory!");
            this.lastName = null;
        }
        if (ucn != null && ucn.length() == 10) this.ucn = ucn;
        else {
            logger.error("Unified Civil number is mandatory and must have 10 digits!");
            this.ucn = null;
        }
        this.phoneNumber = (phoneNumber != null && !phoneNumber.isEmpty()) ? phoneNumber : null;
        this.group = (group != null && !group.isEmpty()) ? group : null;
        role = role.toUpperCase();
        if (role.matches("^(COACH|COMPETITOR)$"))
            this.role = UserRole.valueOf(role);
        else {
            this.role = null;
            logger.error("Role must be coach or competitor!");
        }
        if (clubId != null && !clubId.isEmpty() && clubId.matches("\\d+"))
            this.clubId = Integer.parseInt(clubId);
        else if (clubId != null && !clubId.isEmpty() && !clubId.matches("\\d+")) {
            this.clubId = null;
            logger.error("Invalid input for clubId!");
        }
        this.clubName = (clubName != null && !clubName.isEmpty()) ? clubName.replaceAll("%20", " ") : null;
    }
}
