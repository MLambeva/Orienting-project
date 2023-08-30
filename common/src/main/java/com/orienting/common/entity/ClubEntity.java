package com.orienting.common.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.service.annotation.GetExchange;

@Getter
@Setter
@Entity
@Table(name = "club")
@Data
public class ClubEntity {
    @Id
    @Column(name = "club_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer clubId;

    @Column(name = "clubName")
    private String clubName;

    @Column(name = "coach_id")
    private Integer coachId;

    public ClubEntity() {}
}
