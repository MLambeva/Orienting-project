package com.orienting.common.services;

import com.orienting.common.entity.ClubEntity;
import com.orienting.common.entity.UserEntity;
import com.orienting.common.exception.ExistedCoachException;
import com.orienting.common.exception.InvalidRoleException;
import com.orienting.common.repository.ClubRepository;
import com.orienting.common.repository.UserRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Getter
public class UserClubService {
    private final ClubRepository clubRepository;
    private final UserRepository userRepository;
    @Autowired
    public UserClubService(ClubRepository clubRepository, UserRepository userRepository) {
        this.clubRepository = clubRepository;
        this.userRepository = userRepository;
    }
    public List<ClubEntity> getAllClubs() {
        return clubRepository.findAll();
    }

    public List<UserEntity> getAllUsersByClubId(Integer clubId) {
        return userRepository.findUsersByClubId(clubId);
    }

    public List<UserEntity> getAllUsersByCoachId(Integer coachId) {
        UserEntity coach = userRepository.findUserByUserId(coachId);
        return getAllUsersByClubId(coach.getClubId());
    }

    public ClubEntity createClub(ClubEntity club) {
        if(club.getCoachId() != null && userRepository.findUserByUserId(club.getCoachId()) != null && userRepository.findRoleByUserId(club.getCoachId()).isCoach())
            clubRepository.save(club);
        else if(club.getCoachId() == null)
            clubRepository.save(club);
        else
            throw new InvalidRoleException("Role must be coach!");
        return club;
    }

    public UserEntity createUser(UserEntity user) {
        //String hashPassword = passwordEncoder.encode(user.getPassword());
        //user.setPassword(hashPassword);
        System.out.println(user.getClubId());
        ClubEntity club = clubRepository.findClubByClubId(user.getClubId());
        if(user.isCoach() && club.getCoachId() != null) {
            throw new ExistedCoachException("This club has a coach!");
        }
        userRepository.save(user);
        return user;
    }

    public void updateClub(UserEntity user) {
        ClubEntity club = clubRepository.findClubByClubId(user.getClubId());
        if(club.getCoachId() == null && user.isCoach()) {
            club.setCoachId(user.getUserId());
            clubRepository.save(club);
        }
    }
}
