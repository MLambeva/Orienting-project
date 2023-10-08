package com.orienting.common.services;

import com.orienting.common.entity.ClubEntity;
import com.orienting.common.entity.UserEntity;
import com.orienting.common.exception.InvalidRoleException;
import com.orienting.common.exception.NoExistedClubException;
import com.orienting.common.exception.NoExistedUserException;
import com.orienting.common.repository.ClubRepository;
import com.orienting.common.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Getter
@AllArgsConstructor
public class UserClubService {
    private final UserRepository userRepository;
    private final ClubRepository clubRepository;
    private final PasswordEncoder passwordEncoder;

    public UserEntity findAuthenticatedUser(String email) {
        return userRepository.findByEmail(email).orElseThrow(() ->
                new EntityNotFoundException(String.format("User with email %s does not exist", email)));
    }

    public UserEntity setCoachToClub(Integer userId, Integer clubId) {
        UserEntity user = userRepository.findUserByUserId(userId).orElseThrow(() -> new NoExistedUserException(String.format("User with id: %d does not exist!", userId)));

        if (user.getClub() != null) {
            throw new RuntimeException(String.format("User with id %d is coach to club with id %d!", userId, clubId));
        }
        if (user.isCompetitor()) {
            throw new RuntimeException(String.format("User with id %d is competitor!", userId));
        }
        user.addClub(clubRepository.findClubByClubId(clubId).orElseThrow(() -> new NoExistedClubException(String.format("Club with id %d does not exist!", clubId))));
        return userRepository.save(user);
    }

    public UserEntity addClubToUser(Integer userId, Integer clubId) {
        UserEntity user = userRepository.findUserByUserId(userId).orElseThrow(() -> new NoExistedUserException(String.format("User with userId: %d not existed!", userId)));
        ClubEntity club = clubRepository.findClubByClubId(clubId).orElseThrow(() -> new NoExistedClubException(String.format("Club with clubId %d does not exist!", clubId)));
        if (user.getClub() != null) {
            throw new RuntimeException(String.format("User with user id %d belong to club with club id %d", userId, clubId));
        }
        if(user.isCoach()) {
            throw new RuntimeException(String.format("User with id %d is coach!", userId));
        }
        user.addClub(club);
        return userRepository.save(user);
    }

    private void encodePassword(UserEntity newUser) {
        if(newUser.getPassword() != null) {
            newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        }
    }

    private void validate(UserEntity user, String email, UserEntity newUser) {
        UserEntity del = findAuthenticatedUser(email);
        if (del.isCoach() && user.isCoach()) {
            throw new InvalidRoleException(String.format("Cannot update coach with id %d!", user.getUserId()));
        }
        if (del.isCoach() && (del.getClub() == null || user.getClub() == null)
                || (del.getClub() != null && user.getClub() != null && !Objects.equals(del.getClub().getClubId(), user.getClub().getClubId()))) {
            throw new NoExistedClubException(String.format("Cannot update user with id %d!", user.getUserId()));
        }
        if(newUser.getClub() != null && newUser.getClub().getClubId() != null) {
            ClubEntity club = clubRepository.findClubByClubId(newUser.getClub().getClubId()).orElseThrow();
            newUser.addClub(club);
        }
    }

    private ClubEntity find(UserEntity user) {
        ClubEntity newClub = null;
        if(user.getClub() != null) {
            if(user.getClub().getClubId() != null) {
                newClub = clubRepository.findClubByClubId(user.getClub().getClubId()).orElseThrow(() -> new NoExistedClubException(String.format("Club with id %d does not exist!", user.getClub().getClubId())));
            }
            else if(user.getClub().getClubName() != null){
                newClub = clubRepository.findClubByClubName(user.getClub().getClubName()).orElseThrow(() -> new NoExistedClubException(String.format("Club with name %s does not exist!", user.getClub().getClubName())));
            }
        }
        return newClub;
    }

    public UserEntity updateUserByUserId(Integer userId, String email, UserEntity newUser) {
        UserEntity user = userRepository.findUserByUserId(userId).orElseThrow(() -> new NoExistedUserException(String.format("User with userId: %d does not exist!", userId)));
        validate(user, email, newUser);
        newUser.addClub(find(newUser));
        encodePassword(newUser);
        user.updateUser(newUser);
        return userRepository.save(user);
    }

    public UserEntity updateUserByUcn(String ucn, String email, UserEntity newUser) {
        UserEntity user = userRepository.findUserByUcn(ucn).orElseThrow(() -> new NoExistedUserException(String.format("User with ucn: %s does not exist!", ucn)));
        validate(user, email, newUser);
        newUser.addClub(find(newUser));
        encodePassword(newUser);
        user.updateUser(newUser);
        return userRepository.save(user);
    }

    public UserEntity update(String email, UserEntity newUser) {
        if(newUser.getRole() != null) {
            throw new InvalidRoleException("Cannot change role!");
        }
        newUser.addClub(find(newUser));
        UserEntity user = findAuthenticatedUser(email);
        encodePassword(newUser);
        user.updateUser(newUser);
        return userRepository.save(user);
    }
}
