package com.orienting.common.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "competitors")
public class CompetitorsEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "comp_id")
    private  Integer compId;

    @Column(name = "user_id")
    private Integer userId;

    public CompetitorsEntity(){}
}
