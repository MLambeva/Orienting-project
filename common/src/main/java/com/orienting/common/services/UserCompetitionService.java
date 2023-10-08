package com.orienting.common.services;

import com.orienting.common.entity.CompetitionEntity;
import com.orienting.common.entity.UserEntity;
import com.orienting.common.exception.InvalidRoleException;
import com.orienting.common.exception.NoExistedClubException;
import com.orienting.common.exception.NoExistedCompetition;
import com.orienting.common.exception.NoExistedUserException;
import com.orienting.common.repository.CompetitionRepository;
import com.orienting.common.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.Objects;

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

    private void validate(UserEntity user, String email) {
        UserEntity deletingUser = findAuthenticatedUser(email);
        if (deletingUser.isCoach() && user.isCoach()) {
            throw new InvalidRoleException(String.format("Cannot manage coach with id %d!", user.getUserId()));
        }
        if (deletingUser.isCoach() && (deletingUser.getClub() == null || user.getClub() == null)
                || (deletingUser.getClub() != null && user.getClub() != null && !Objects.equals(deletingUser.getClub().getClubId(), user.getClub().getClubId()))) {
            throw new NoExistedClubException(String.format("Cannot manage user with id %d!", user.getUserId()));
        }
    }

    private UserEntity request(UserEntity user, CompetitionEntity competition, String email) {
        validate(user, email);
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

    public UserEntity requestParticipation(Integer userId, Integer compId, String email) {
        UserEntity user = userRepository.findUserByUserId(userId).orElseThrow(() -> new NoExistedUserException(String.format("User with userId: %d does not exist!", userId)));
        CompetitionEntity competition = competitionRepository.findCompetitionByCompId(compId).orElseThrow(() -> new NoExistedCompetition(String.format("Competition with id %d does not exist!", compId)));
        return request(user, competition, email);
    }


    public UserEntity requestParticipationByName(Integer userId, String name, String email) {
        UserEntity user = userRepository.findUserByUserId(userId).orElseThrow(() -> new NoExistedUserException(String.format("User with userId: %d does not exist!", userId)));
        CompetitionEntity competition = competitionRepository.findCompetitionByName(name).orElseThrow(() -> new NoExistedCompetition(String.format("Competition with name %s does not exist!", name)));
        return request(user, competition, email);
    }

    private UserEntity removeParticipant(UserEntity user, CompetitionEntity competition, String email) {
        validate(user, email);
        if(LocalDate.now().isAfter(competition.getDeadline())) {
            throw new IllegalArgumentException("Cannot remove participation!");
        }
        user.removeCompetition(competition);
        return userRepository.save(user);
    }

    public UserEntity removeParticipationById(Integer userId, Integer compId, String email) {
        UserEntity user = userRepository.findUserByUserId(userId).orElseThrow(() -> new NoExistedUserException(String.format("User with userId: %d does not exist!", userId)));
        CompetitionEntity competition = competitionRepository.findCompetitionByCompId(compId).orElseThrow(() -> new NoExistedCompetition(String.format("Competition with id %d does not exist!", compId)));
        return removeParticipant(user, competition, email);
    }


    public UserEntity removeParticipationByName(Integer userId, String name, String email) {
        UserEntity user = userRepository.findUserByUserId(userId).orElseThrow(() -> new NoExistedUserException(String.format("User with userId: %d does not exist!", userId)));
        CompetitionEntity competition = competitionRepository.findCompetitionByName(name).orElseThrow(() -> new NoExistedCompetition(String.format("Competition with name %s does not exist!", name)));
        return removeParticipant(user, competition, email);
    }
}
