package com.orienting.common.dto;

import com.orienting.common.enums.UserRole;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ClubDto {
    private Integer clubId;
    @NotBlank(message = "Club name is mandatory!")
    private String clubName;
    @NotBlank(message = "Club city is mandatory!")
    private String city;

    public ClubDto() {}

    public ClubDto(String clubName, String city) {
        this.clubName = (clubName != null && !clubName.isEmpty() ?  clubName.replaceAll("%20", " ") : null);
        this.city = (city != null && !city.isEmpty() ?  city.replaceAll("%20", " ") : null);
    }
}
