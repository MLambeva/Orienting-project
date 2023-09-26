package com.orienting.common.repository;

import java.util.List;
import java.util.Optional;

import com.orienting.common.entity.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<TokenEntity, Integer> {

    @Query("SELECT t FROM TokenEntity t INNER JOIN t.user u " +
            "WHERE u.id = :id AND (t.expired = false OR t.revoked = false)")
    List<TokenEntity> findAllValidTokenByUser(Integer id);

    Optional<TokenEntity> findByToken(String token);
}