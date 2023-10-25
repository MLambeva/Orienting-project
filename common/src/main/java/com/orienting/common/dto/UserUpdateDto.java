package com.orienting.common.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.orienting.common.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;

@Getter
@Setter
@NoArgsConstructor
public class UserUpdateDto {
    @Email(message = "Invalid email!")
    private String email;
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).{8,}$", message = "Invalid password!")
    private String password;
    private String firstName;
    private String lastName;
    @Pattern(regexp = "^(\\+\\d{1,2})?\\s?\\(?\\d{3}\\)?[-.\\s]?\\d{3}[-.\\s]?\\d{4}$", message = "Invalid phone number!")
    private String phoneNumber;
    @Pattern(regexp = "^(W|M)\\d{2}$", message = "Invalid format! Format must be in format M/W(dd), d-digit --> for example: M21")
    private String group;
    private UserRole role;
    private Integer clubId;
    private String clubName;
    @JsonIgnore
    private Logger logger = LoggerFactory.getLogger(UserUpdateDto.class);

    public UserUpdateDto(String email, String password, String firstName, String lastName, String phoneNumber, String group, String clubId, String clubName) {
        this.email = (email != null && !email.isEmpty()) ? email : null;
        this.password = (password != null && !password.isEmpty()) ? password : null;
        this.firstName = (firstName != null && !firstName.isEmpty()) ? firstName : null;
        this.lastName = (lastName != null && !lastName.isEmpty()) ? lastName : null;
        this.phoneNumber = (phoneNumber != null && !phoneNumber.isEmpty()) ? phoneNumber : null;
        this.group = (group != null && !group.isEmpty()) ? group : null;
        if(clubId != null &&  !clubId.isEmpty() && clubId.matches("\\d+"))
            this.clubId = Integer.parseInt(clubId);
        else if(clubId != null &&  !clubId.isEmpty() && !clubId.matches("\\d+")) {
            this.clubId = null;
            logger.error("Invalid input for clubId!");
        }
        if(clubName != null && !clubName.isEmpty()) this.clubName = clubName.replaceAll("%20", " ");
    }
}
