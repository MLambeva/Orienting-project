package com.orienting.common.dto;

import com.orienting.common.enums.UserRole;
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
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).{8,}$", message = "Invalid password!")
    private String password;
    @NotBlank(message = "First name is mandatory!")
    private String firstName;
    @NotBlank(message = "Last name is mandatory!")
    private String lastName;
    @Pattern(regexp = "^\\d{10}$", message = "UCN have 10 digits")
    @NotBlank(message = "Unified Civil number is mandatory!")
    private String ucn;
    @Pattern(regexp = "^(\\+\\d{1,2})?\\s?\\(?\\d{3}\\)?[-.\\s]?\\d{3}[-.\\s]?\\d{4}$", message = "Invalid phone number!")
    private String phoneNumber;
    @Pattern(regexp = "^(W|M)\\d{2}$", message = "Invalid format of group -> M/W{digit}!")
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
        if (clubId != null) this.clubId = clubId;
    }

    public UserCreationDto(String email, String password, String firstName, String lastName, String ucn, String phoneNumber, String group, String role, String clubId, String clubName) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.ucn = ucn;
        this.phoneNumber = phoneNumber;
        this.group = group;
        if(role.matches("^(COACH|COMPETITOR)$"))
            this.role = UserRole.valueOf(role);
        else
            System.err.println("Role must be coach or competitor!");
        if(clubId != null &&  !clubId.isEmpty() && clubId.matches("\\d+"))
            this.clubId = Integer.parseInt(clubId);
        else if(clubId != null &&  !clubId.isEmpty() && !clubId.matches("\\d+")) {
            this.clubId = null;
            System.err.println("Invalid input for clubId!");
        }
        if(clubName != null && !clubName.isEmpty()) this.clubName = clubName.replaceAll("%20", " ");
    }
}
