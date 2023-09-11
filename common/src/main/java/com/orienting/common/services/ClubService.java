package com.orienting.common.services;

import com.orienting.common.entity.ClubEntity;
import com.orienting.common.entity.UserEntity;
import com.orienting.common.exception.NoExistedClub;
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
        return clubRepository.findAllWithUsers();
    }

    public ClubEntity getClubById(Integer clubId) {
        return clubRepository.findClubByClubId(clubId).orElseThrow(() -> new NoExistedClub(String.format("Club with clubId %d does not exist!", clubId)));
    }

    public ClubEntity createClub(ClubEntity club) {
        clubRepository.save(club);
        return club;
    }

    public void deleteClub(Integer clubId) {
        ClubEntity club = clubRepository.findClubByClubId(clubId).orElseThrow(() -> new NoExistedClub(String.format("Club with clubId %d does not exist!", clubId)));
        clubRepository.delete(club);
    }

    public void updateClub(Integer clubId, ClubEntity newClub) {
        ClubEntity club = clubRepository.findClubByClubId(clubId).orElseThrow(() -> new NoExistedClub(String.format("Club with clubId %d does not exist!", clubId)));
        club.updateClub(newClub);
        clubRepository.save(club);
    }



}
