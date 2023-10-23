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
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Service
@Getter
@AllArgsConstructor
public class UserCompetitionService {
    private final UserRepository userRepository;
    private final CompetitionRepository competitionRepository;

    public UserEntity findAuthenticatedUser(String email) {
        return userRepository.findUserByEmail(email).orElseThrow(() ->
                new NoExistedUserException(String.format("User with email %s does not exist", email)));
    }

    private void validateAccessToUser(UserEntity user, String email) {
        UserEntity authUser = findAuthenticatedUser(email);
        if (authUser.isCoach() && user.isCoach() && !Objects.equals(authUser.getUserId(), user.getUserId())) {
            throw new InvalidRoleException(String.format("Cannot manage coach with id %d!", user.getUserId()));
        }
        if (authUser.isCoach() && (authUser.getClub() == null || user.getClub() == null)
                || (authUser.getClub() != null && user.getClub() != null && !Objects.equals(authUser.getClub().getClubId(), user.getClub().getClubId()))) {
            throw new InvalidRoleException("Cannot manage user with that id!");
        }
    }

    private UserEntity request(UserEntity user, CompetitionEntity competition, String email) {
        validateAccessToUser(user, email);
        if(user.isRequestedInCompetition(competition)) {
            throw new InvalidInputException("User has already request!");
        }
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        if(LocalDate.now().isAfter(competition.getDeadline())) {
            throw new InvalidInputException(String.format("Cannot request participation! The deadline is %s!", competition.getDeadline().format(dateFormatter)));
        }
        if (user.getClub() != null) {
            user.addCompetition(competition);
            return userRepository.save(user);
        } else {
            throw new NoExistedClubException("User does not have club!");
        }
    }

    public UserEntity requestParticipationById(Integer userId, Integer compId, String email) {
        UserEntity user = userRepository.findUserByUserId(userId).orElseThrow(() -> new NoExistedUserException("User with that id does not exist!"));
        CompetitionEntity competition = competitionRepository.findCompetitionByCompId(compId).orElseThrow(() -> new NoExistedCompetitionException("Competition with that id does not exist!"));
        return request(user, competition, email);
    }


    public UserEntity requestParticipationByName(Integer userId, String name, String email) {
        UserEntity user = userRepository.findUserByUserId(userId).orElseThrow(() -> new NoExistedUserException("User with that id does not exist!"));
        CompetitionEntity competition = competitionRepository.findCompetitionByName(name).orElseThrow(() -> new NoExistedCompetitionException(String.format("Competition with name %s does not exist!", name)));
        return request(user, competition, email);
    }

    private UserEntity removeParticipant(UserEntity user, CompetitionEntity competition, String email) {
        validateAccessToUser(user, email);
        if(LocalDate.now().isAfter(competition.getDeadline())) {
            throw new InvalidInputException("Cannot remove participation!");
        }
        user.removeCompetition(competition);
        return userRepository.save(user);
    }

    public UserEntity removeParticipationById(Integer userId, Integer compId, String email) {
        UserEntity user = userRepository.findUserByUserId(userId).orElseThrow(() -> new NoExistedUserException("User with that id does not exist!"));
        CompetitionEntity competition = competitionRepository.findCompetitionByCompId(compId).orElseThrow(() -> new NoExistedCompetitionException("Competition with that id does not exist!"));
        return removeParticipant(user, competition, email);
    }


    public UserEntity removeParticipationByName(Integer userId, String name, String email) {
        UserEntity user = userRepository.findUserByUserId(userId).orElseThrow(() -> new NoExistedUserException("User with that id does not exist!"));
        CompetitionEntity competition = competitionRepository.findCompetitionByName(name).orElseThrow(() -> new NoExistedCompetitionException(String.format("Competition with name %s does not exist!", name)));
        return removeParticipant(user, competition, email);
    }
}
