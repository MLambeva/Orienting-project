package com.orienting.common.services;

import com.orienting.common.entity.UserEntity;
import com.orienting.common.exception.InvalidRoleException;
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
                new EntityNotFoundException(String.format("User with id: %d does not exits!", userId)));
    }
    public UserEntity getUserByUcn(String ucn) {
        return userRepository.findUserByUcn(ucn).orElseThrow(() ->
                new EntityNotFoundException(String.format("User with unified civil number: %s does not exits!", ucn)));
    }
    public List<UserEntity> getAllUsersByClubId(Integer clubId) {
        return userRepository.findAllUsersInClub(clubId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Club with id %d is empty!", clubId)));
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
            throw new EntityNotFoundException(String.format("User with userId: %d not existed!", userId));
        }
    }

    public String getRoleByUcn(String ucn) {
        Optional<UserEntity> userOptional = userRepository.findUserByUcn(ucn);

        if (userOptional.isPresent()) {
            return userOptional.get().getRole();
        } else {
            throw new EntityNotFoundException(String.format("User with unified civil number %s not existed!", ucn));
        }
    }


    public UserEntity deleteAndUpdateByHelper(String identifier, String identifierType, Boolean isAdmin, String action) {
        if (identifier == null || identifierType == null) {
            throw new IllegalArgumentException("Identifier and identifierType cannot be null.");
        }
        Optional<UserEntity> user;
        if ("userId".equals(identifierType)) {
            Integer userId = Integer.parseInt(identifier);
            user = userRepository.findUserByUserId(userId);
        } else if ("ucn".equals(identifierType))
            user = userRepository.findUserByUcn(identifier);
        else
            throw new IllegalArgumentException("Invalid identifierType: " + identifierType);

        if (user.isEmpty())
            throw new EntityNotFoundException("User not found!");

        if (user.get().isCoach() && !isAdmin) {
            throw new InvalidRoleException("Role must be competitor!");
        }

        if ("delete".equals(action)) {
            userRepository.delete(user.get());
        }
        else if(!"update".equals(action)) {
            throw new RuntimeException("Action must be delete or update!");
        }

        return user.get();
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

}
