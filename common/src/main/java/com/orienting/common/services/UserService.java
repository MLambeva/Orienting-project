package com.orienting.common.services;

import com.orienting.common.entity.*;
import com.orienting.common.exception.InvalidRoleException;
import com.orienting.common.exception.NoExistedUserException;
import com.orienting.common.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Getter
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserEntity findAuthenticatedUser(String email) {
        return userRepository.findByEmail(email).orElseThrow(() ->
                new EntityNotFoundException(String.format("User with email %s does not exist", email)));
    }
    public List<UserEntity> getUsers() {
        return userRepository.findAll();
    }

    public UserEntity getUserById(Integer userId) {
        return userRepository.findUserByUserId(userId).orElseThrow(() ->
                new NoExistedUserException(String.format("User with id %d does not exist!", userId)));
    }

    public UserEntity getUserByUcn(String ucn) {
        return userRepository.findUserByUcn(ucn).orElseThrow(() ->
                new NoExistedUserException(String.format("User with unified civil number %s does not exist!", ucn)));
    }

    public String getRoleByUserId(Integer userId) {
        Optional<UserEntity> userOptional = userRepository.findUserByUserId(userId);

        if (userOptional.isPresent()) {
            return userOptional.get().getRole().name();
        } else {
            throw new NoExistedUserException(String.format("User with userId: %d does not exist!", userId));
        }
    }

    public String getRoleByUcn(String ucn) {
        Optional<UserEntity> userOptional = userRepository.findUserByUcn(ucn);

        if (userOptional.isPresent()) {
            return userOptional.get().getRole().name();
        } else {
            throw new NoExistedUserException(String.format("User with unified civil number %s does not exist!", ucn));
        }
    }

    public List<UserEntity> getAllCoaches() {
        return userRepository.findAll().stream()
                .filter(UserEntity::isCoach).toList();
    }

    public List<UserEntity> getAllCompetitors() {
        return userRepository.findAll().stream()
                .filter(UserEntity::isCompetitor).toList();
    }


    public List<UserEntity> getCoachesByUserId(Integer userId) {
        UserEntity user = userRepository.findUserByUserId(userId).orElseThrow(() -> new NoExistedUserException(String.format("User with userId: %d does not exist!", userId)));
        ClubEntity club = user.getClub();
        if (club == null) {
            throw new RuntimeException(String.format("User with id %d does not belong to club!", userId));
        }
        if (user.isCoach()) {
            throw new RuntimeException("User must be competitor!");
        }
        return club.getUsers().stream().filter(UserEntity::isCoach).toList();
    }

    public List<CompetitionEntity> getCompetitionsByUserId(Integer userId) {
        UserEntity user = userRepository.findUserByUserId(userId).orElseThrow(() -> new NoExistedUserException(String.format("User with userId: %d does not exist!", userId)));
        return user.getCompetitions().stream().toList();
    }
    public List<UserEntity> getAllUsersInClubByClubId(Integer clubId) {
        return userRepository.findAllUsersInClubByClubId(clubId).orElseThrow(() -> new NoExistedUserException("Club is empty!"));
    }

    public List<UserEntity> getAllCompetitorsInClubByClubId(Integer clubId) {
        return userRepository.findAllUsersInClubByClubId(clubId).orElseThrow(() -> new NoExistedUserException("The club does not have competitors!")).stream().filter(UserEntity::isCompetitor).toList();
    }

    public List<UserEntity> getAllCoachesInClubByClubId(Integer clubId) {
        return userRepository.findAllUsersInClubByClubId(clubId).orElseThrow(() -> new NoExistedUserException("The club does not have coaches!")).stream().filter(UserEntity::isCoach).toList();
    }

    public List<UserEntity> getAllUsersInClubByClubName(String clubName) {
        return userRepository.findAllUsersInClubByName(clubName).orElseThrow(() -> new NoExistedUserException("Club is empty!"));
    }

    public List<UserEntity> getAllCompetitorsInClubByClubName(String clubName) {
        return userRepository.findAllUsersInClubByName(clubName).orElseThrow(() -> new NoExistedUserException("The club does not have competitors!")).stream().filter(UserEntity::isCompetitor).toList();
    }

    public List<UserEntity> getAllCoachesInClubByClubName(String clubName) {
        return userRepository.findAllUsersInClubByName(clubName).orElseThrow(() -> new NoExistedUserException("The club does not have coaches!")).stream().filter(UserEntity::isCoach).toList();
    }

    public UserEntity deleteAndUpdateByHelper(String identifier, String identifierType, Boolean isAdmin, String action) {
        if (identifier == null || identifierType == null) {
            throw new IllegalArgumentException("Identifier and identifierType cannot be null.");
        }
        UserEntity user;
        if ("userId".equals(identifierType)) {
            Integer userId = Integer.parseInt(identifier);
            user = userRepository.findUserByUserId(userId).orElseThrow(() -> new NoExistedUserException(String.format("User with userId: %s does not exist!", identifier)));
        } else if ("ucn".equals(identifierType))
            user = userRepository.findUserByUcn(identifier).orElseThrow(() -> new NoExistedUserException(String.format("User with unified civil number %s does not exist!", identifier)));
        else
            throw new IllegalArgumentException("Invalid identifierType: " + identifierType);

        if (user.isCoach() && !isAdmin) {
            throw new InvalidRoleException("Role must be competitor!");
        }

        if ("delete".equals(action)) {
            userRepository.delete(user);
        } else if (!"update".equals(action)) {
            throw new RuntimeException("Action must be delete or update!");
        }

        return user;
    }

    public UserEntity deleteUserByUserId(Integer userId, Boolean isAdmin) {
        return deleteAndUpdateByHelper(userId.toString(), "userId", isAdmin, "delete");
    }

    public UserEntity deleteUserByUcn(String ucn, Boolean isAdmin) {
        return deleteAndUpdateByHelper(ucn, "ucn", isAdmin, "delete");
    }

    public UserEntity updateUserBy(String identifier, String identifierType, Boolean isAdmin, UserEntity newUser) {
        UserEntity user = deleteAndUpdateByHelper(identifier, identifierType, isAdmin, "update");
        user.updateUser(newUser);
        return userRepository.save(user);
    }

    public UserEntity updateUserByUserId(Integer userId, Boolean isAdmin, UserEntity newUser) {
        return updateUserBy(userId.toString(), "userId", isAdmin, newUser);
    }

    public UserEntity updateUserByUcn(String ucn, Boolean isAdmin, UserEntity newUser) {
        return updateUserBy(ucn, "ucn", isAdmin, newUser);
    }

    public UserEntity leftClub(Integer userId) {
        UserEntity user = userRepository.findUserByUserId(userId).orElseThrow(() -> new NoExistedUserException(String.format("User with id: %s does not exist!", userId)));
        user.leftClub();
        return userRepository.save(user);
    }

    public UserEntity makeCoach(Integer userId) {
        UserEntity user = userRepository.findUserByUserId(userId).orElseThrow(() -> new NoExistedUserException(String.format("User with id %d does not exist!", userId)));
        ;
        if (user.isCoach()) {
            throw new InvalidRoleException("Role must be competitor!");
        }
        user.setRole(UserRole.COACH);
        return userRepository.save(user);
    }

    public UserEntity removeCoach(Integer userId) {
        UserEntity user = userRepository.findUserByUserId(userId).orElseThrow(() -> new NoExistedUserException(String.format("User with id %d does not exist!", userId)));
        if (user.isCompetitor()) {
            throw new InvalidRoleException("Role must be coach!");
        }
        user.setRole(UserRole.COMPETITOR);
        return userRepository.save(user);
    }
}
