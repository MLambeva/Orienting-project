package com.orienting.service.services;

import com.orienting.service.entity.ClubEntity;
import com.orienting.service.entity.UserEntity;
import com.orienting.service.exception.InvalidInputException;
import com.orienting.service.exception.InvalidRoleException;
import com.orienting.service.exception.NoExistedClubException;
import com.orienting.service.exception.NoExistedUserException;
import com.orienting.service.repository.ClubRepository;
import com.orienting.service.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@Getter
@AllArgsConstructor
public class UserClubService {
    private final UserRepository userRepository;
    private final ClubRepository clubRepository;
    private final PasswordEncoder passwordEncoder;

    public UserEntity findAuthenticatedUser(String email) {
        return userRepository.findUserByEmail(email).orElseThrow(() ->
                new NoExistedUserException(String.format("User with email %s does not exist", email)));
    }

    public List<UserEntity> getAllUsersInClubByClubId(Integer clubId) {
        clubRepository.findClubByClubId(clubId).orElseThrow(() -> new NoExistedClubException("Club with that id does not exist!"));
        return userRepository.findAllUsersInClubByClubId(clubId).orElseThrow(() -> new NoExistedUserException("Club is empty!"));
    }

    public List<UserEntity> getAllCompetitorsInClubByClubId(Integer clubId) {
        clubRepository.findClubByClubId(clubId).orElseThrow(() -> new NoExistedClubException("Club with that id does not exist!"));
        return userRepository.findAllUsersInClubByClubId(clubId).orElseThrow(() -> new NoExistedUserException("The club does not have competitors!")).stream().filter(UserEntity::isCompetitor).toList();
    }

    public List<UserEntity> getAllCoachesInClubByClubId(Integer clubId) {
        clubRepository.findClubByClubId(clubId).orElseThrow(() -> new NoExistedClubException("Club with that id does not exist!"));
        return userRepository.findAllUsersInClubByClubId(clubId).orElseThrow(() -> new NoExistedUserException("The club does not have coaches!")).stream().filter(UserEntity::isCoach).toList();
    }

    public List<UserEntity> getAllUsersInClubByClubName(String clubName) {
        clubRepository.findClubByClubName(clubName).orElseThrow(() -> new NoExistedClubException("Club with that name does not exist!"));
        return userRepository.findAllUsersInClubByName(clubName).orElseThrow(() -> new NoExistedUserException("Club is empty!"));
    }

    public List<UserEntity> getAllCompetitorsInClubByClubName(String clubName) {
        clubRepository.findClubByClubName(clubName).orElseThrow(() -> new NoExistedClubException("Club with that name does not exist!"));
        return userRepository.findAllUsersInClubByName(clubName).orElseThrow(() -> new NoExistedUserException("The club does not have competitors!")).stream().filter(UserEntity::isCompetitor).toList();
    }

    public List<UserEntity> getAllCoachesInClubByClubName(String clubName) {
        clubRepository.findClubByClubName(clubName).orElseThrow(() -> new NoExistedClubException("Club with that name does not exist!"));
        return userRepository.findAllUsersInClubByName(clubName).orElseThrow(() -> new NoExistedUserException("The club does not have coaches!")).stream().filter(UserEntity::isCoach).toList();
    }

    public UserEntity setCoachToClub(Integer userId, Integer clubId) {
        UserEntity user = userRepository.findUserByUserId(userId).orElseThrow(() -> new NoExistedUserException("User with that id does not exist!"));

        if (user.getClub() != null) {
            throw new InvalidInputException("User with that id is coach to club!");
        }
        if (user.isCompetitor()) {
            throw new InvalidRoleException("User with that id is competitor!");
        }
        user.addClub(clubRepository.findClubByClubId(clubId).orElseThrow(() -> new NoExistedClubException(String.format("Club with id %d does not exist!", clubId))));
        return userRepository.save(user);
    }

    public UserEntity addClubToUser(Integer userId, Integer clubId) {
        UserEntity user = userRepository.findUserByUserId(userId).orElseThrow(() -> new NoExistedUserException("User with tha id does not existed!"));
        ClubEntity club = clubRepository.findClubByClubId(clubId).orElseThrow(() -> new NoExistedClubException("Club with that id does not exist!"));
        if (user.getClub() != null) {
            throw new InvalidInputException("User belong to club!");
        }
        user.addClub(club);
        return userRepository.save(user);
    }

    private void encodePassword(UserEntity newUser) {
        if(newUser.getPassword() != null) {
            newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        }
    }

    private void validateAccessUserForUpdate(UserEntity user, String email, UserEntity newUser) {
        UserEntity authUser = findAuthenticatedUser(email);
        if (authUser.isCoach() && user.isCoach()) {
            throw new InvalidRoleException("Cannot update that coach!");
        }
        if (authUser.isCoach() && (authUser.getClub() == null || user.getClub() == null)
                || (authUser.getClub() != null && user.getClub() != null && !Objects.equals(authUser.getClub().getClubId(), user.getClub().getClubId()))) {
            throw new NoExistedClubException("Cannot update user with that id!");
        }
        if(newUser.getClub() != null && newUser.getClub().getClubId() != null) {
            ClubEntity club = clubRepository.findClubByClubId(newUser.getClub().getClubId()).orElseThrow(() -> new NoExistedClubException("Club with that id does not exist!"));
            newUser.addClub(club);
        }
    }

    private ClubEntity find(UserEntity user) {
        ClubEntity newClub = null;
        if(user.getClub() != null) {
            if(user.getClub().getClubId() != null) {
                newClub = clubRepository.findClubByClubId(user.getClub().getClubId()).orElseThrow(() -> new NoExistedClubException("Club with that id does not exist!"));
            }
            else if(user.getClub().getClubName() != null){
                newClub = clubRepository.findClubByClubName(user.getClub().getClubName()).orElseThrow(() -> new NoExistedClubException(String.format("Club with name %s does not exist!", user.getClub().getClubName())));
            }
        }
        return newClub;
    }

    public UserEntity updateUserByUserId(Integer userId, String email, UserEntity newUser) {
        UserEntity user = userRepository.findUserByUserId(userId).orElseThrow(() -> new NoExistedUserException("User with that id does not exist!"));
        validateAccessUserForUpdate(user, email, newUser);
        newUser.addClub(find(newUser));
        encodePassword(newUser);
        user.updateUser(newUser);
        return userRepository.save(user);
    }

    public UserEntity updateUserByUcn(String ucn, String email, UserEntity newUser) {
        UserEntity user = userRepository.findUserByUcn(ucn).orElseThrow(() -> new NoExistedUserException(String.format("User with ucn: %s does not exist!", ucn)));
        validateAccessUserForUpdate(user, email, newUser);
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

    public UserEntity addUser(UserEntity user) {
        if (user == null) {
            throw new InvalidInputException("Input user is null!");
        }
        if (userRepository.findUserByEmail(user.getEmail()).isPresent()) {
            throw new InvalidInputException(String.format("User with email %s already exist!", user.getEmail()));
        }
        ClubEntity newClub = null;
        if (user.getClub() != null) {
            if (user.getClub().getClubId() != null) {
                newClub = clubRepository.findClubByClubId(user.getClub().getClubId()).orElseThrow(() -> new NoExistedClubException(String.format("Club with id %d does not exist!", user.getClub().getClubId())));
            } else if (user.getClub().getClubName() != null) {
                newClub = clubRepository.findClubByClubName(user.getClub().getClubName()).orElseThrow(() -> new NoExistedClubException(String.format("Club with name %s does not exist!", user.getClub().getClubName())));
            }
        }
        UserEntity newUser = UserEntity.builder()
                .email(user.getEmail())
                .password(passwordEncoder.encode(user.getPassword()))
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .ucn(user.getUcn())
                .phoneNumber(user.getPhoneNumber())
                .group(user.getGroup())
                .role(user.getRole())
                .club(newClub)
                .build();
        return userRepository.save(newUser);
    }
}
