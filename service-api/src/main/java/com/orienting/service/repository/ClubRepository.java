package com.orienting.service.repository;

import com.orienting.service.entity.ClubEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClubRepository extends JpaRepository<ClubEntity, Integer> {
    Optional<ClubEntity> findClubByClubId(Integer clubId);

    Optional<ClubEntity> findClubByClubName(String clubName);

    @Query("SELECT DISTINCT c FROM ClubEntity c LEFT JOIN FETCH c.users")
    List<ClubEntity> findAllWithUsers();
}
