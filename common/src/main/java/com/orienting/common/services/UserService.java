package com.orienting.common.services;

import com.orienting.common.entity.UserEntity;
import com.orienting.common.exception.InvalidRoleException;
import com.orienting.common.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Getter
public class UserService {

    private final UserRepository userRepository;
    //private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        //this.passwordEncoder = passwordEncoder;
    }

    public List<UserEntity> getUsers() {
        return userRepository.findAll();
    }

    public UserEntity getUserById(Integer id) {
        return userRepository.findUserByUserId(id);
    }

    //Coach & admin, but admin can delete coaches?
    public UserEntity deleteUserBy(String identifier, String identifierType) {
        if (identifier == null || identifierType == null) {
            throw new IllegalArgumentException("Identifier and identifierType cannot be null.");
        }
        UserEntity user;
        if ("userId".equals(identifierType)) {
            Integer userId = Integer.parseInt(identifier);
            user = userRepository.findUserByUserId(userId);
        } else if ("ucn".equals(identifierType))
            user = userRepository.findUserByUcn(identifier);
        else
            throw new IllegalArgumentException("Invalid identifierType: " + identifierType);

        if (user == null)
            throw new EntityNotFoundException("User not found!");

        if (!("competitor".equals(user.getRole())))
            throw new InvalidRoleException("Role must be competitor!");

        userRepository.delete(user);
        return user;
    }

    public UserEntity deleteUserByUserId(Integer userId) {
        return deleteUserBy(userId.toString(), "userId");
    }

    public UserEntity deleteUserByUcn(String ucn) {
        return deleteUserBy(ucn, "ucn");
    }
}
