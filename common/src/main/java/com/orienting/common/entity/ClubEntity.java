package com.orienting.common.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
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


    public ClubEntity(String clubName, String city) {
        this.clubName = clubName;
        this.city = city;
    }

    public void updateClub(ClubEntity club) {
        if(club != null) {
            if(club.clubName != null) {
                this.setClubName(club.getClubName());
            }
            if(club.city != null) {
                this.setCity(club.getCity());
            }
        }
    }

}
