package com.orienting.service.repository;

import com.orienting.service.entity.CompetitionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CompetitionRepository extends JpaRepository<CompetitionEntity, Integer> {
    Optional<List<CompetitionEntity>> findCompetitionByDate(LocalDate date);
    Optional<CompetitionEntity> findCompetitionByName(String name);
    Optional<CompetitionEntity> findCompetitionByCompId(Integer compId);
}
