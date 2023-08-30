package com.orienting.common.dto;
import lombok.Data;

import java.util.Date;
@Data
public class UserDto {
    private Integer userId;
    private String email;
    private String name;
    private String surname;
    private String ucn;
    private String phoneNumber;
    private Date birthDate;
    private Integer clubId;
    private String group;
    private String role;
}
