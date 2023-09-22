package com.orienting.common.repository;

import com.orienting.common.entity.AuthenticationTokenEntity;
import com.orienting.common.entity.UserEntity;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<AuthenticationTokenEntity, Integer> {
    Optional<AuthenticationTokenEntity> findTokenByToken(String token);
    Optional<AuthenticationTokenEntity> findTokenByUser(UserEntity user);
}
