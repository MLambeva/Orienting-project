package com.orienting.common.services;

import com.orienting.common.entity.CompetitionEntity;
import com.orienting.common.exception.NoExistedCompetition;
import com.orienting.common.repository.CompetitionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Getter
@AllArgsConstructor
public class CompetitionService {
    private final CompetitionRepository competitionRepository;

    public List<CompetitionEntity> getAllCompetitions() {
        return competitionRepository.findAll();
    }

    public List<CompetitionEntity> getCompetitionByDate(LocalDate date) {
        return competitionRepository.findCompetitionByDate(date).orElseThrow(() -> new NoExistedCompetition(String.format("Competitions on %s do not exist!", date.toString())));
    }

    public CompetitionEntity createCompetition(CompetitionEntity competition) {
        if(competition == null) {
            throw new IllegalArgumentException("Input competition is null!");
        }
        return competitionRepository.save(competition);
    }

    public CompetitionEntity deleteCompById(Integer compId) {
        CompetitionEntity competition = competitionRepository.findCompetitionByCompId(compId).orElseThrow(() -> new NoExistedCompetition(String.format("Competition with id %d does not exist!", compId)));
        if(competition == null) {
            throw new EntityNotFoundException("Competition not found!");
        }
        competitionRepository.delete(competition);
        return competition;
    }

    public CompetitionEntity updateCompetition(Integer compId, CompetitionEntity competition) {
        CompetitionEntity competitionEntity = competitionRepository.findCompetitionByCompId(compId).orElseThrow(() -> new NoExistedCompetition(String.format("Competition with id %d does not exist!", compId)));
        competitionEntity.update(competition);
        return competitionRepository.save(competitionEntity);
    }
}
