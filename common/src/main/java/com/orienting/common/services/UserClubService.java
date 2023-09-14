package com.orienting.common.services;

import com.orienting.common.entity.ClubEntity;
import com.orienting.common.entity.UserEntity;
import com.orienting.common.exception.InvalidRoleException;
import com.orienting.common.exception.NoExistedClubException;
import com.orienting.common.exception.NoExistedUserException;
import com.orienting.common.repository.ClubRepository;
import com.orienting.common.repository.UserRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Getter
public class UserClubService {
    private final UserRepository userRepository;
    private final ClubRepository clubRepository;
    //private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserClubService(UserRepository userRepository, ClubRepository clubRepository) {
        this.userRepository = userRepository;
        this.clubRepository = clubRepository;
    }

    public UserEntity createUser(UserEntity user) {
        //String hashPassword = passwordEncoder.encode(user.getPassword());
        //user.setPassword(hashPassword);
        if(user == null) {
            throw new IllegalArgumentException("Input user is null!");
        }
        //if(user.getClub() == null && user.isCompetitor()) {
        //    throw new RuntimeException("Competitor must belong to club!");
        //}
        if (user.getClub() != null) {
            Integer clubId = user.getClub().getClubId();
            String clubName = user.getClub().getClubName();
            ClubEntity club;
            if(clubId != null) {
                club = clubRepository.findClubByClubId(clubId).orElseThrow(() ->
                        new NoExistedClubException(String.format("Club with id %d does not exist!", clubId)));
            }
            else if(clubName != null) {
                club = clubRepository.findClubByClubName(clubName).orElseThrow(() ->
                        new NoExistedClubException(String.format("Club with name %s does not exist!", clubName)));
            }
            else {
                throw new NoExistedClubException("");
            }
            user.addClub(club);
        }
        return userRepository.save(user);
    }


    public UserEntity makeCoach(Integer userId) {
        UserEntity user = userRepository.findUserByUserId(userId).orElseThrow(() -> new NoExistedUserException(String.format("User with id %d does not exist!", userId)));;
        if(user.isCoach()) {
            throw new InvalidRoleException("Role must be competitor!");
        }
        user.setRole("coach");
        return userRepository.save(user);
    }

    public UserEntity removeCoach(Integer userId) {
        UserEntity user = userRepository.findUserByUserId(userId).orElseThrow(() -> new NoExistedUserException(String.format("User with id %d does not exist!", userId)));;
        if(user.isCompetitor()) {
            throw new InvalidRoleException("Role must be coach!");
        }
        user.setRole("competitor");
        return userRepository.save(user);
    }

    public UserEntity setCoachToClub(Integer userId, Integer clubId) {
        UserEntity user = userRepository.findUserByUserId(userId).orElseThrow(() -> new NoExistedUserException(String.format("User with id: %d does not exist!", userId)));;
        if(user.getClub() == null) {
            throw new RuntimeException(String.format("User with id %d should belong to club and then to be coach!", userId));
        }
        if(user.isCompetitor()) {
            user.setRole("coach");
        }
        user.addClub(clubRepository.findClubByClubId(clubId).orElseThrow(() -> new NoExistedClubException(String.format("Club with id %d does not exist!", clubId))));
        return userRepository.save(user);
    }

    public UserEntity addClubToUser(Integer userId, Integer clubId) {
        UserEntity user = userRepository.findUserByUserId(userId).orElseThrow(() -> new NoExistedUserException(String.format("User with userId: %d not existed!", userId)));
        ClubEntity club = clubRepository.findClubByClubId(clubId).orElseThrow(() -> new NoExistedClubException(String.format("Club with clubId %d does not exist!", clubId)));
        if(user.getClub() != null) {
            throw new RuntimeException(String.format("User with user id %d belong to club with club id %d", userId, clubId));
        }
        user.addClub(club);
        return userRepository.save(user);
    }

}
