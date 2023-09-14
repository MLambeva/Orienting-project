package com.orienting.common.services;

import com.orienting.common.entity.CompetitionEntity;
import com.orienting.common.entity.UserEntity;
import com.orienting.common.exception.NoExistedClubException;
import com.orienting.common.exception.NoExistedCompetition;
import com.orienting.common.exception.NoExistedUserException;
import com.orienting.common.repository.CompetitionRepository;
import com.orienting.common.repository.UserRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Getter
public class UserCompetitionService {
    private final UserRepository userRepository;
    private final CompetitionRepository competitionRepository;

    @Autowired
    public UserCompetitionService(UserRepository userRepository, CompetitionRepository competitionRepository) {
        this.userRepository = userRepository;
        this.competitionRepository = competitionRepository;
    }

    public void requestParticipation(Integer userId, Integer compId) {
        UserEntity user = userRepository.findUserByUserId(userId).orElseThrow(() -> new NoExistedUserException(String.format("User with userId: %d does not exist!", userId)));
        CompetitionEntity competition = competitionRepository.findCompetitionByCompId(compId).orElseThrow(() -> new NoExistedCompetition(String.format("Competition with id %d does not exist!", compId)));
        if(user.getClub() != null) {
            user.addCompetition(competition);
            userRepository.save(user);
        }
        else {
            throw new NoExistedClubException("User does not have club!");
        }
    }

    public UserEntity removeParticipation(Integer userId, Integer compId) {
        UserEntity user = userRepository.findUserByUserId(userId).orElseThrow(() -> new NoExistedUserException(String.format("User with userId: %d does not exist!", userId)));
        CompetitionEntity competition = competitionRepository.findCompetitionByCompId(compId).orElseThrow(() -> new NoExistedCompetition(String.format("Competition with id %d does not exist!", compId)));
        user.removeCompetition(competition);
        return userRepository.save(user);
    }
}
