package com.orienting.common.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;
    @Column(name = "email")
    private String email;
    @Column(name = "password")
    private String password;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "ucn")
    private String ucn;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "race_group")
    private String group;
    @Column(name = "role")
    private String role;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_clubs",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "club_id")
    )
    private ClubEntity club;

    @ManyToMany
    @JoinTable(
            name = "users_competitions",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "comp_id")
    )
    private Set<CompetitionEntity> competitions;

    public UserEntity(){}

    public boolean isCoach() {
        return "coach".equals(role);
    }

    public boolean isCompetitor() {
        return "competitor".equals(role);
    }

    public void addClub(ClubEntity club) {
        this.club = club;
    }

    public void leftClub() { this.club = null; }

    public void addCompetition(CompetitionEntity competition) {
        this.competitions.add(competition);
    }

    public void removeCompetition(CompetitionEntity competition) {this.competitions.remove(competition);}
    public void updateUser(UserEntity newUser) {
        if (newUser != null) {
            if (newUser.getEmail() != null) {
                this.setEmail(newUser.getEmail());
            }
            if (newUser.getPassword() != null) {
                this.setPassword(newUser.getPassword());
            }
            if (newUser.getFirstName() != null) {
                this.setFirstName(newUser.getFirstName());
            }
            if (newUser.getLastName() != null) {
                this.setLastName(newUser.getLastName());
            }
            if (newUser.getPhoneNumber() != null) {
                this.setPhoneNumber(newUser.getPhoneNumber());
            }
            if (newUser.getGroup() != null) {
                this.setGroup(newUser.getGroup());
            }
            if (newUser.getRole() != null) {
                this.setRole(newUser.getRole());
            }
            if (newUser.getClub() != null) {
                this.setClub(newUser.getClub());
            }
        }
    }
}
