package com.orienting.common.services;

import com.orienting.common.entity.CompetitionEntity;
import com.orienting.common.entity.UserEntity;
import com.orienting.common.exception.NoExistedClubException;
import com.orienting.common.exception.NoExistedCompetition;
import com.orienting.common.exception.NoExistedUserException;
import com.orienting.common.repository.CompetitionRepository;
import com.orienting.common.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

@Service
@Getter
@AllArgsConstructor
public class UserCompetitionService {
    private final UserRepository userRepository;
    private final CompetitionRepository competitionRepository;

    public UserEntity findAuthenticatedUser(String email) {
        return userRepository.findByEmail(email).orElseThrow(() ->
                new EntityNotFoundException(String.format("User with email %s does not exist", email)));
    }

    public UserEntity requestParticipation(Integer userId, Integer compId) {
        UserEntity user = userRepository.findUserByUserId(userId).orElseThrow(() -> new NoExistedUserException(String.format("User with userId: %d does not exist!", userId)));
        CompetitionEntity competition = competitionRepository.findCompetitionByCompId(compId).orElseThrow(() -> new NoExistedCompetition(String.format("Competition with id %d does not exist!", compId)));
        if(LocalDate.now().isAfter(competition.getDeadline())) {
            throw new IllegalArgumentException("Cannot request participation!");
        }
        if (user.getClub() != null) {
            user.addCompetition(competition);
            return userRepository.save(user);
        } else {
            throw new NoExistedClubException("User does not have club!");
        }
    }

    public UserEntity removeParticipation(Integer userId, Integer compId) {
        UserEntity user = userRepository.findUserByUserId(userId).orElseThrow(() -> new NoExistedUserException(String.format("User with userId: %d does not exist!", userId)));
        CompetitionEntity competition = competitionRepository.findCompetitionByCompId(compId).orElseThrow(() -> new NoExistedCompetition(String.format("Competition with id %d does not exist!", compId)));
        if(LocalDate.now().isAfter(competition.getDeadline())) {
            throw new IllegalArgumentException("Cannot remove participation!");
        }
        user.removeCompetition(competition);
        return userRepository.save(user);
    }
}
