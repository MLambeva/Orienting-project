package com.orienting.common.repository;

import com.orienting.common.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    Optional<UserEntity> findUserByUcn(String ucn);

    Optional<UserEntity> findUserByUserId(Integer userId);

    @Query("SELECT u FROM UserEntity u WHERE u.club.clubId = :clubId")
    Optional<List<UserEntity>> findAllUsersInClub(@Param("clubId") Integer clubId);

    @Query("SELECT u FROM UserEntity u WHERE u.club.clubId = :clubId AND u.role = :role")
    Optional<List<UserEntity>> findAllUsersByRoleInClub(@Param("clubId") Integer clubId, @Param("role") String role);
}
