package com.orienting.common.services;

import com.orienting.common.entity.UserEntity;
import com.orienting.common.exception.NoExistedClubWithClubId;
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
            if (clubRepository.findClubByClubId(clubId) == null) {
                throw new NoExistedClubWithClubId(String.format("Club with clubId %d not existed!", clubId));
            }
        }
        return userRepository.save(user);
    }




}
