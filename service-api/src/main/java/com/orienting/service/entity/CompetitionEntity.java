package com.orienting.service.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
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
    @Column(name = "deadline")
    private LocalDate deadline;
    @Column(name = "location")
    private String location;
    @Column(name = "coordinates")
    private String coordinates;
    @ManyToMany(mappedBy = "competitions")
    private Set<UserEntity> users;

    public void update(CompetitionEntity competition) {
        if(competition != null) {
            if(competition.getName() != null) {
                this.setName(competition.getName());
            }
            if(competition.getDate() != null) {
                this.setDate(competition.getDate());
            }
            if(competition.getTime() != null) {
                this.setTime(competition.getTime());
            }
            if(competition.getDeadline() != null) {
                this.setDeadline(competition.getDeadline());
            }
            if(competition.getLocation() != null) {
                this.setLocation(competition.getLocation());
            }
            if(competition.getCoordinates() != null) {
                this.setCoordinates(competition.getCoordinates());
            }
        }
    }
}
