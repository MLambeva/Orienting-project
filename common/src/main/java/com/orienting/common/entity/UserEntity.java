package com.orienting.common.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;


@Getter
@Setter
@Entity
@Table(name = "users")
@Data
public class UserEntity {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;
    @Column(name = "email")
    private String email;
    @Column(name = "password")
    private String password;
    @Column(name = "name")
    private String name;
    @Column(name = "surname")
    private String surname;
    @Column(name = "ucn")
    private String ucn;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "club_id")
    private Integer clubId;
    @Column(name = "race_group")
    private String group;
    @Column(name = "role")
    private String role;

    public UserEntity(){}

}
