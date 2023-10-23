package com.orienting.service.services;

import com.orienting.service.entity.ClubEntity;
import com.orienting.service.entity.CompetitionEntity;
import com.orienting.service.entity.UserEntity;
import com.orienting.common.enums.UserRole;
import com.orienting.service.exception.InvalidInputException;
import com.orienting.service.exception.InvalidRoleException;
import com.orienting.service.exception.NoExistedClubException;
import com.orienting.service.exception.NoExistedUserException;
import com.orienting.service.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@Getter
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserEntity findAuthenticatedUser(String email) {
        return userRepository.findUserByEmail(email).orElseThrow(() ->
                new NoExistedUserException(String.format("User with email %s does not exist", email)));
    }

    public List<UserEntity> getUsers() {
        return userRepository.findAll();
    }

    public UserEntity getUserById(Integer userId) {
        return userRepository.findUserByUserId(userId).orElseThrow(() ->
                new NoExistedUserException("User with that id does not exist!"));
    }

    public UserEntity getUserByUcn(String ucn) {
        return userRepository.findUserByUcn(ucn).orElseThrow(() ->
                new NoExistedUserException(String.format("User with unified civil number %s does not exist!", ucn)));
    }

    public String getRoleByUserId(Integer userId) {
        UserEntity user = userRepository.findUserByUserId(userId).orElseThrow(() -> new NoExistedUserException("User with that id does not exist!"));
        return user.getRole().name();
    }

    public String getRoleByUcn(String ucn) {
        UserEntity user = userRepository.findUserByUcn(ucn).orElseThrow(() -> new NoExistedUserException(String.format("User with unified civil number %s does not exist!", ucn)));
        return user.getRole().name();
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
        UserEntity user = userRepository.findUserByUserId(userId).orElseThrow(() -> new NoExistedUserException("User with that id does not exist!"));
        ClubEntity club = user.getClub();
        if (club == null) {
            throw new NoExistedClubException("User with that id does not belong to club!");
        }
        if (user.isCoach()) {
            throw new InvalidRoleException("User must be competitor!");
        }
        return club.getUsers().stream().filter(UserEntity::isCoach).toList();
    }

    public List<CompetitionEntity> getCompetitionsByUserId(Integer userId) {
        UserEntity user = userRepository.findUserByUserId(userId).orElseThrow(() -> new NoExistedUserException("User with that id does not exist!"));
        return user.getCompetitions().stream().toList();
    }

    private void validateAccessUser(UserEntity user, String email) {
        UserEntity authUser = findAuthenticatedUser(email);
        if(Objects.equals(user.getUserId(), authUser.getUserId())) {
            throw new InvalidInputException("User cannot delete yourself!");
        }
        if (authUser.isCoach() && user.isCoach()) {
            throw new InvalidRoleException(String.format("Cannot delete coach with id %d!", user.getUserId()));
        }
        if (authUser.isCoach() && (authUser.getClub() == null || user.getClub() == null)
        || (authUser.getClub() != null && user.getClub() != null && !Objects.equals(authUser.getClub().getClubId(), user.getClub().getClubId()))) {
                throw new InvalidRoleException("Cannot delete user with that id!");
        }
    }

    public UserEntity deleteUserByUserId(Integer userId, String email) {
        UserEntity user = userRepository.findUserByUserId(userId).orElseThrow(() -> new NoExistedUserException("User with that id does not exist!"));
        validateAccessUser(user, email);
        userRepository.delete(user);
        return user;
    }

    public UserEntity deleteUserByUcn(String ucn, String email) {
        UserEntity user = userRepository.findUserByUcn(ucn).orElseThrow(() -> new NoExistedUserException(String.format("User with ucn: %s does not exist!", ucn)));
        validateAccessUser(user, email);
        userRepository.delete(user);
        return user;
    }

    public UserEntity leftClub(Integer userId) {
        UserEntity user = userRepository.findUserByUserId(userId).orElseThrow(() -> new NoExistedUserException("User with that id does not exist!"));
        if(user.getClub() != null) {
            user.leftClub();
            return userRepository.save(user);
        }
        else {
            throw new NoExistedClubException("User does not belong to club!");
        }
    }

    public UserEntity makeCoach(Integer userId) {
        UserEntity user = userRepository.findUserByUserId(userId).orElseThrow(() -> new NoExistedUserException("User with that id does not exist!"));
        if (user.isCoach()) {
            throw new InvalidRoleException("Role must be competitor!");
        }
        user.setRole(UserRole.COACH);
        return userRepository.save(user);
    }

    public UserEntity removeCoach(Integer userId) {
        UserEntity user = userRepository.findUserByUserId(userId).orElseThrow(() -> new NoExistedUserException("User with that id does not exist!"));
        if (user.isCompetitor()) {
            throw new InvalidRoleException("Role must be coach!");
        }
        user.setRole(UserRole.COMPETITOR);
        return userRepository.save(user);
    }
}