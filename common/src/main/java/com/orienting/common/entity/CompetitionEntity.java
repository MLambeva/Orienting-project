package com.orienting.common.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "competition")
public class CompetitionEntity {
    @Id
    @Column(name = "comp_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer compId;

    @Column(name = "name")
    private String name;

    @JsonFormat(pattern = "dd-MM-yyyy")
    @Column(name = "date")
    private LocalDate date;

    @Column(name = "time")
    private LocalTime time;

    @Column(name = "location")
    private String location;

    @Column(name = "coordinates")
    private String coordinates;

    @ManyToMany(mappedBy = "competitions")
    private Set<UserEntity> users;

    public CompetitionEntity() {}
}
