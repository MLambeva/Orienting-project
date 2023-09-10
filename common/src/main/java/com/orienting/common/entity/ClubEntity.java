package com.orienting.common.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "clubs")
public class ClubEntity {
    @Id
    @Column(name = "club_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer clubId;

    @Column(name = "club_name")
    private String clubName;

    @Column(name = "city")
    private String city;

    @OneToMany(mappedBy = "club")
    private Set<UserEntity> users;


}
