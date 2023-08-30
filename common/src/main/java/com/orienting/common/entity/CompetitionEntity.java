package com.orienting.common.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Time;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "competition")
public class CompetitionEntity {
    @Id
    @Column(name = "comp_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer comp_id;

    @Column(name = "name")
    private String name;

    @Column(name = "date")
    private Date date;

    @Column(name = "time")
    private Time time;

    @Column(name = "location")
    private String location;

    @Column(name = "length")
    private Integer length;

    @Column(name = "coordinates")
    private String coordinates;

    public CompetitionEntity() {}
}
