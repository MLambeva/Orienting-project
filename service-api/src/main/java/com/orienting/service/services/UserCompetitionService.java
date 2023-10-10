package com.orienting.service.services;

import com.orienting.service.entity.CompetitionEntity;
import com.orienting.service.entity.UserEntity;
import com.orienting.service.exception.*;
import com.orienting.service.repository.CompetitionRepository;
import com.orienting.service.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Getter;
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
                new NoExistedUserException(String.format("User with email %s does not exist", email)));
    }

    private void validate(UserEntity user, String email) {
        UserEntity deletingUser = findAuthenticatedUser(email);
        if (deletingUser.isCoach() && user.isCoach() && !Objects.equals(deletingUser.getUserId(), user.getUserId())) {
            throw new InvalidRoleException(String.format("Cannot manage coach with id %d!", user.getUserId()));
        }
        if (deletingUser.isCoach() && (deletingUser.getClub() == null || user.getClub() == null)
                || (deletingUser.getClub() != null && user.getClub() != null && !Objects.equals(deletingUser.getClub().getClubId(), user.getClub().getClubId()))) {
            throw new InvalidRoleException(String.format("Cannot manage user with id %d!", user.getUserId()));
        }
    }

    private UserEntity request(UserEntity user, CompetitionEntity competition, String email) {
        validate(user, email);
        if(user.isRequestedInCompetition(competition)) {
            throw new InvalidInputException("User has already request!");
        }
        if(LocalDate.now().isAfter(competition.getDeadline())) {
            throw new InvalidInputException("Cannot request participation!");
        }
        if (user.getClub() != null) {
            user.addCompetition(competition);
            return userRepository.save(user);
        } else {
            throw new NoExistedClubException("User does not have club!");
        }
    }

    public UserEntity requestParticipationById(Integer userId, Integer compId, String email) {
        UserEntity user = userRepository.findUserByUserId(userId).orElseThrow(() -> new NoExistedUserException(String.format("User with userId: %d does not exist!", userId)));
        CompetitionEntity competition = competitionRepository.findCompetitionByCompId(compId).orElseThrow(() -> new NoExistedCompetitionException(String.format("Competition with id %d does not exist!", compId)));
        return request(user, competition, email);
    }


    public UserEntity requestParticipationByName(Integer userId, String name, String email) {
        UserEntity user = userRepository.findUserByUserId(userId).orElseThrow(() -> new NoExistedUserException(String.format("User with userId: %d does not exist!", userId)));
        CompetitionEntity competition = competitionRepository.findCompetitionByName(name).orElseThrow(() -> new NoExistedCompetitionException(String.format("Competition with name %s does not exist!", name)));
        return request(user, competition, email);
    }

    private UserEntity removeParticipant(UserEntity user, CompetitionEntity competition, String email) {
        validate(user, email);
        if(LocalDate.now().isAfter(competition.getDeadline())) {
            throw new InvalidInputException("Cannot remove participation!");
        }
        user.removeCompetition(competition);
        return userRepository.save(user);
    }

    public UserEntity removeParticipationById(Integer userId, Integer compId, String email) {
        UserEntity user = userRepository.findUserByUserId(userId).orElseThrow(() -> new NoExistedUserException(String.format("User with userId: %d does not exist!", userId)));
        CompetitionEntity competition = competitionRepository.findCompetitionByCompId(compId).orElseThrow(() -> new NoExistedCompetitionException(String.format("Competition with id %d does not exist!", compId)));
        return removeParticipant(user, competition, email);
    }


    public UserEntity removeParticipationByName(Integer userId, String name, String email) {
        UserEntity user = userRepository.findUserByUserId(userId).orElseThrow(() -> new NoExistedUserException(String.format("User with userId: %d does not exist!", userId)));
        CompetitionEntity competition = competitionRepository.findCompetitionByName(name).orElseThrow(() -> new NoExistedCompetitionException(String.format("Competition with name %s does not exist!", name)));
        return removeParticipant(user, competition, email);
    }
}
