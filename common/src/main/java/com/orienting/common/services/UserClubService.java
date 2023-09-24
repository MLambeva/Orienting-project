package com.orienting.common.services;
import com.orienting.common.entity.ClubEntity;
import com.orienting.common.entity.UserRole;
import com.orienting.common.entity.UserEntity;
import com.orienting.common.exception.InvalidInputException;
import com.orienting.common.exception.NoExistedClubException;
import com.orienting.common.exception.NoExistedUserException;
import com.orienting.common.repository.ClubRepository;
import com.orienting.common.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Getter
@AllArgsConstructor
public class UserClubService {
    private final UserRepository userRepository;
    private final ClubRepository clubRepository;
    private final PasswordEncoder passwordEncoder;

    public UserEntity createUser(UserEntity user) throws Exception {
        if (user == null) {
            throw new IllegalArgumentException("Input user is null!");
        }
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new InvalidInputException(String.format("User with email %s already exist!", user.getEmail()));
        }
        String hashPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashPassword);

        if (user.getClub() != null) {
            Integer clubId = user.getClub().getClubId();
            String clubName = user.getClub().getClubName();
            ClubEntity club;
            if (clubId != null) {
                club = clubRepository.findClubByClubId(clubId).orElseThrow(() ->
                        new NoExistedClubException(String.format("Club with id %d does not exist!", clubId)));
            } else if (clubName != null) {
                club = clubRepository.findClubByClubName(clubName).orElseThrow(() ->
                        new NoExistedClubException(String.format("Club with name %s does not exist!", clubName)));
            } else {
                throw new NoExistedClubException("");
            }
            user.addClub(club);
        }
        return userRepository.save(user);
    }

    public UserEntity setCoachToClub(Integer userId, Integer clubId) {
        UserEntity user = userRepository.findUserByUserId(userId).orElseThrow(() -> new NoExistedUserException(String.format("User with id: %d does not exist!", userId)));
        ;
        if (user.getClub() == null) {
            throw new RuntimeException(String.format("User with id %d should belong to club and then to be coach!", userId));
        }
        if (user.isCompetitor()) {
            user.setRole(UserRole.COACH);
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
        user.addClub(club);
        return userRepository.save(user);
    }
}
