package com.orienting.common.services;
import com.orienting.common.entity.UserEntity;
import com.orienting.common.repository.UserRepository;
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

    public void createUser(UserEntity user) {
        //String hashPassword = passwordEncoder.encode(user.getPassword());
        //user.setPassword(hashPassword);
        System.out.println(user);
        userRepository.save(user);
    }
}
