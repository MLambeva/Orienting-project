package com.orienting.common.services;

import com.orienting.common.entity.UserEntity;
import com.orienting.common.exception.InvalidRoleException;
import com.orienting.common.exception.NoExistedUserException;
import com.orienting.common.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Getter
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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
    public List<UserEntity> getAllUsersByClubId(Integer clubId) {
        return userRepository.findAllUsersInClub(clubId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Club with id %d has no members!", clubId)));
    }

    public List<UserEntity> getAllCoachesByClubId(Integer clubId, String role) {
        return userRepository.findAllUsersByRoleInClub(clubId, role).orElseThrow(() ->
                new EntityNotFoundException(String.format("Club with id %d does not have users with role %s!", clubId, role)));
    }

    public String getRoleByUserId(Integer userId) {
        Optional<UserEntity> userOptional = userRepository.findUserByUserId(userId);

        if (userOptional.isPresent()) {
            return userOptional.get().getRole();
        } else {
            throw new NoExistedUserException(String.format("User with userId: %d does not exist!", userId));
        }
    }

    public String getRoleByUcn(String ucn) {
        Optional<UserEntity> userOptional = userRepository.findUserByUcn(ucn);

        if (userOptional.isPresent()) {
            return userOptional.get().getRole();
        } else {
            throw new NoExistedUserException(String.format("User with unified civil number %s does not exist!", ucn));
        }
    }

    public List<UserEntity> getAllCoaches() {
        return userRepository.findAll().stream()
                .filter(UserEntity::isCoach).toList();
    }

    public UserEntity deleteAndUpdateByHelper(String identifier, String identifierType, Boolean isAdmin, String action) {
        if (identifier == null || identifierType == null) {
            throw new IllegalArgumentException("Identifier and identifierType cannot be null.");
        }
        UserEntity user;
        if ("userId".equals(identifierType)) {
            Integer userId = Integer.parseInt(identifier);
            user = userRepository.findUserByUserId(userId).orElseThrow(() -> new NoExistedUserException(String.format("User with userId: %d does not exist!", identifier)));
        } else if ("ucn".equals(identifierType))
            user = userRepository.findUserByUcn(identifier).orElseThrow(() -> new NoExistedUserException(String.format("User with unified civil number %s does not exist!", identifier)));
        else
            throw new IllegalArgumentException("Invalid identifierType: " + identifierType);

        if (user.isCoach() && !isAdmin) {
            throw new InvalidRoleException("Role must be competitor!");
        }

        if ("delete".equals(action)) {
            userRepository.delete(user);
        }
        else if(!"update".equals(action)) {
            throw new RuntimeException("Action must be delete or update!");
        }

        return user;
    }

    public void deleteUserByUserId(Integer userId, Boolean isAdmin) {
        deleteAndUpdateByHelper(userId.toString(), "userId", isAdmin, "delete");
    }

    public void deleteUserByUcn(String ucn, Boolean isAdmin) {
        deleteAndUpdateByHelper(ucn, "ucn", isAdmin, "delete");
    }

    public void updateUserBy(String identifier, String identifierType, Boolean isAdmin, UserEntity newUser) {
        UserEntity user = deleteAndUpdateByHelper(identifier, identifierType, isAdmin, "update");
        user.updateUser(newUser);
        userRepository.save(user);
    }

    public void updateUserByUserId(Integer userId, Boolean isAdmin, UserEntity newUser) {
        updateUserBy(userId.toString(), "userId", isAdmin, newUser);
    }

    public void updateUserByUcn(String ucn, Boolean isAdmin, UserEntity newUser) {
        updateUserBy(ucn, "ucn", isAdmin, newUser);
    }
}
