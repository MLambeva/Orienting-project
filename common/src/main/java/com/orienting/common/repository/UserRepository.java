package com.orienting.common.repository;

import com.orienting.common.entity.UserEntity;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    UserEntity findRoleByUserId(Integer userId);
    UserEntity findUserByUcn(String ucn);
    UserEntity findUserByUserId(Integer userId);
    List<UserEntity> findUsersByClubId(Integer clubId);
}
