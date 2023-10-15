package com.orienting.service.services;

import com.orienting.service.entity.CompetitionEntity;
import com.orienting.service.exception.InvalidInputException;
import com.orienting.service.exception.NoExistedCompetitionException;
import com.orienting.service.repository.CompetitionRepository;
import jakarta.persistence.EntityExistsException;
import lombok.AllArgsConstructor;
import lombok.Getter;
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
        return competitionRepository.findCompetitionByDate(date).orElseThrow(() -> new NoExistedCompetitionException(String.format("Competitions on %s do not exist!", date.toString())));
    }

    public CompetitionEntity getCompetitionByName(String name) {
        return competitionRepository.findCompetitionByName(name).orElseThrow(() -> new NoExistedCompetitionException(String.format("Competition with name %s does not exist!", name)));
    }

    public CompetitionEntity getCompetitionByCompId(Integer compId) {
        return competitionRepository.findCompetitionByCompId(compId).orElseThrow(() -> new NoExistedCompetitionException(String.format("Competition with id %s does not exist!", compId)));
    }

    private void validateIfCompetitionExist(CompetitionEntity competition) {
        if(competitionRepository.findCompetitionByName(competition.getName()).isPresent()) {
            throw new EntityExistsException("Competition with that name exist!");
        }
    }
    public CompetitionEntity createCompetition(CompetitionEntity competition) {
        if (competition == null) {
            throw new InvalidInputException("Competition is null!");
        }
        validateIfCompetitionExist(competition);
        if (competition.getDate().isBefore(competition.getDeadline())) {
            throw new InvalidInputException("Deadline must be before date of competition!");
        }
        return competitionRepository.save(competition);
    }

    private void validateAndDeleteCompetition(CompetitionEntity competition) {
        if (competition == null) {
            throw new InvalidInputException("Competition not found!");
        }
        competitionRepository.delete(competition);
    }

    public CompetitionEntity deleteCompById(Integer compId) {
        CompetitionEntity competition = competitionRepository.findCompetitionByCompId(compId).orElseThrow(() -> new NoExistedCompetitionException(String.format("Competition with id %d does not exist!", compId)));
        validateAndDeleteCompetition(competition);
        return competition;
    }

    public CompetitionEntity deleteCompByName(String name) {
        CompetitionEntity competition = competitionRepository.findCompetitionByName(name).orElseThrow(() -> new NoExistedCompetitionException(String.format("Competition with name %s does not exist!", name)));
        validateAndDeleteCompetition(competition);
        return competition;
    }

    private void validateAndUpdateCompetition(CompetitionEntity competitionEntity, CompetitionEntity competition) {
        if ((competition.getDeadline() != null && competition.getDate() == null && competitionEntity.getDate().isBefore(competition.getDeadline()))
                || (competition.getDate() != null && competition.getDeadline() == null && competition.getDate().isBefore(competitionEntity.getDeadline()))
                || (competition.getDate() != null && competition.getDeadline() != null && competition.getDate().isBefore(competition.getDeadline()))) {
            throw new InvalidInputException("Deadline must be before the date of the competition!");
        }
        competitionEntity.update(competition);
    }

    public CompetitionEntity updateCompetitionById(Integer compId, CompetitionEntity competition) {
        validateIfCompetitionExist(competition);
        CompetitionEntity competitionEntity = competitionRepository.findCompetitionByCompId(compId).orElseThrow(() -> new NoExistedCompetitionException(String.format("Competition with id %d does not exist!", compId)));
        validateAndUpdateCompetition(competitionEntity, competition);
        return competitionRepository.save(competitionEntity);
    }

    public CompetitionEntity updateCompetitionByName(String name, CompetitionEntity competition) {
        validateIfCompetitionExist(competition);
        CompetitionEntity competitionEntity = competitionRepository.findCompetitionByName(name).orElseThrow(() -> new NoExistedCompetitionException(String.format("Competition with name %s does not exist!", name)));
        validateAndUpdateCompetition(competitionEntity, competition);
        return competitionRepository.save(competitionEntity);
    }
}
