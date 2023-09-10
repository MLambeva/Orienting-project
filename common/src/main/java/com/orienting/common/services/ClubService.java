package com.orienting.common.services;

import com.orienting.common.entity.ClubEntity;
import com.orienting.common.repository.ClubRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Getter
public class ClubService {
    private final ClubRepository clubRepository;

    @Autowired
    public ClubService(ClubRepository clubRepository) {
        this.clubRepository = clubRepository;
    }

    public List<ClubEntity> getAllClubs() {
        return clubRepository.findAll();
    }

    public ClubEntity getClubById(Integer clubId) {
        return clubRepository.findClubByClubId(clubId);
    }


    public ClubEntity createClub(ClubEntity club) {
        clubRepository.save(club);
        return club;
    }

}
