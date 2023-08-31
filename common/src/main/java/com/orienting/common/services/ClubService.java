package com.orienting.common.services;

import com.orienting.common.entity.ClubEntity;
import com.orienting.common.exception.InvalidRoleException;
import com.orienting.common.repository.ClubRepository;
import com.orienting.common.repository.UserRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Getter
public class ClubService {
    private final ClubRepository clubRepository;
    private final UserRepository userRepository;
    @Autowired
    public ClubService(ClubRepository clubRepository, UserRepository userRepository) {
        this.clubRepository = clubRepository;
        this.userRepository = userRepository;
    }
    public List<ClubEntity> getAllClubs() {
        return clubRepository.findAll();
    }

    public void createClub(ClubEntity club) {
        if(club.getCoachId() != null && "coach".equals(userRepository.findRoleByUserId(club.getCoachId()).getRole()))
            clubRepository.save(club);
        else if(club.getCoachId() == null)
            clubRepository.save(club);
        else
            throw new InvalidRoleException("Role must be coach!");
    }

}
