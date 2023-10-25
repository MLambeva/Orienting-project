package com.orienting.service.entity;

import com.orienting.common.enums.UserRole;
import com.orienting.service.exception.NoExistedCompetitionException;
import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class UserEntity implements UserDetails {

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
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private UserRole role;

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

    @OneToMany(mappedBy = "user")
    private Set<TokenEntity> tokens;

    public UserEntity(String email, String password, String firstName, String lastName, String ucn, String phoneNumber, String group, UserRole role, Integer clubId) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.ucn = ucn;
        this.phoneNumber = phoneNumber;
        this.group = group;
        this.role = role;
        if (clubId != null) this.club = new ClubEntity(clubId);
    }

    public UserEntity(String email, String password, String firstName, String lastName, String ucn, UserRole role) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.ucn = ucn;
        this.role = role;
    }

    public boolean isCoach() {
        return this.role.equals(UserRole.COACH);
    }

    public boolean isCompetitor() {
        return this.role.equals(UserRole.COMPETITOR);
    }

    public boolean isAdmin() {
        return this.role.equals(UserRole.ADMIN);
    }

    public void addClub(ClubEntity club) {
        this.club = club;
    }

    public void leftClub() {
        this.club = null;
    }

    public void addCompetition(CompetitionEntity competition) {
        this.competitions.add(competition);
    }

    public void removeCompetition(CompetitionEntity competition) {
        if (!competitions.contains(competition)) {
            throw new NoExistedCompetitionException("User does not have request for this competition!");
        }
        this.competitions.remove(competition);
    }

    public boolean isRequestedInCompetition(CompetitionEntity competition) {
        return this.competitions.contains(competition);
    }

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
            if (newUser.getClub() != null) {
                this.setClub(newUser.getClub());
            }
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.role.name()));
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
