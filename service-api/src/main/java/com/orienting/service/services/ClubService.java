package com.orienting.service.services;

import com.orienting.service.entity.ClubEntity;
import com.orienting.service.exception.InvalidInputException;
import com.orienting.service.exception.NoExistedClubException;
import com.orienting.service.repository.ClubRepository;
import jakarta.persistence.EntityExistsException;
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
        return clubRepository.findClubByClubId(clubId).orElseThrow(() -> new NoExistedClubException(String.format("Club with id %d does not exist!", clubId)));
    }

    public ClubEntity getClubByName(String clubName) {
        return clubRepository.findClubByClubName(clubName).orElseThrow(() -> new NoExistedClubException(String.format("Club with name %s does not exist!", clubName)));
    }

    private void validateIfClubExist(ClubEntity club) {
        if(clubRepository.findClubByClubName(club.getClubName()).isPresent()) {
            throw new EntityExistsException("Club with that name already exist!");
        }
    }
    public ClubEntity createClub(ClubEntity club) {
        if (club == null) {
            throw new InvalidInputException("Club is null!");
        }
        validateIfClubExist(club);
        ClubEntity clubEntity = new ClubEntity(club.getClubName(), club.getCity());
        return clubRepository.save(clubEntity);
    }

    public ClubEntity deleteClubById(Integer clubId) {
        ClubEntity club = clubRepository.findClubByClubId(clubId).orElseThrow(() -> new NoExistedClubException(String.format("Club with id %d does not exist!", clubId)));
        clubRepository.delete(club);
        return club;
    }

    public ClubEntity deleteClubByName(String clubName) {
        ClubEntity club = clubRepository.findClubByClubName(clubName).orElseThrow(() -> new NoExistedClubException(String.format("Club with name %s does not exist!", clubName)));
        clubRepository.delete(club);
        return club;
    }

    public ClubEntity updateClubById(Integer clubId, ClubEntity newClub) {
        validateIfClubExist(newClub);
        ClubEntity club = clubRepository.findClubByClubId(clubId).orElseThrow(() -> new NoExistedClubException(String.format("Club with id %d does not exist!", clubId)));
        club.updateClub(newClub);
        return clubRepository.save(club);
    }

    public ClubEntity updateClubByName(String clubName, ClubEntity newClub) {
        validateIfClubExist(newClub);
        ClubEntity club = clubRepository.findClubByClubName(clubName).orElseThrow(() -> new NoExistedClubException(String.format("Club with name %s does not exist!", clubName)));
        club.updateClub(newClub);
        return clubRepository.save(club);
    }
}
