package com.orienting.common.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.relational.core.sql.In;

@Getter
@Setter
@Entity
@Table(name = "competitors")
@Data
public class CompetitorsEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "comp_id")
    private  Integer compId;

    @Column(name = "user_id")
    private Integer userId;

    public CompetitorsEntity(){}
}
