package com.orienting.common.repository;

import com.orienting.common.entity.ClubEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ClubRepository  extends JpaRepository<ClubEntity, Integer> {
    ClubEntity findClubByClubId(Integer clubId);
}
