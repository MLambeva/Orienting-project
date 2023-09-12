package com.orienting.common.services;

import com.orienting.common.entity.ClubEntity;
import com.orienting.common.entity.UserEntity;
import com.orienting.common.exception.InvalidRoleException;
import com.orienting.common.exception.NoExistedClub;
import com.orienting.common.exception.NoExistedUser;
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
        if (user.getClub() != null) {
            Integer clubId = user.getClub().getClubId();
            ClubEntity club = clubRepository.findClubByClubId(clubId).orElseThrow(() ->
                    new NoExistedClub(String.format("Club with id: %d does not exist!", user.getClub().getClubId())));
            user.addClub(club);
        }
        return userRepository.save(user);
    }

    public UserEntity addCoach(Integer clubId, UserEntity user) {
        ClubEntity club = clubRepository.findClubByClubId(clubId).orElseThrow(() -> new NoExistedClub(String.format("Club with clubId %d does not exist!", clubId)));
        user.addClub(club);
        user.setRole("coach");
        return userRepository.save(user);
    }

    public void makeAndChangeCoach(Integer userId, Integer clubId) {
        UserEntity user = userRepository.findUserByUserId(userId).orElseThrow(() -> new NoExistedUser(String.format("User with userId: %d does not exist!", userId)));;
        addCoach(clubId, user);
    }

    public void makeCoach(Integer userId) {
        UserEntity user = userRepository.findUserByUserId(userId).orElseThrow(() -> new NoExistedUser(String.format("User with userId: %d does not exist!", userId)));;
        if(user.getClub() == null) {
            throw new RuntimeException(String.format("User with id %d should belong to club and then to be coach!", userId));
        }
        if(user.isCoach()) {
            throw new InvalidRoleException("Role must be competitor!");
        }
        user.setRole("coach");
        userRepository.save(user);
    }

    public void addClubToUser(Integer userId, Integer clubId) {
        UserEntity user = userRepository.findUserByUserId(userId).orElseThrow(() -> new NoExistedUser(String.format("User with userId: %d not existed!", userId)));
        ClubEntity club = clubRepository.findClubByClubId(clubId).orElseThrow(() -> new NoExistedClub(String.format("Club with clubId %d does not exist!", clubId)));
        if(user.getClub() != null) {
            throw new RuntimeException(String.format("User with user id: %d belong to club with club id: %d", userId, clubId));
        }
        user.addClub(club);
        userRepository.save(user);
    }

}
