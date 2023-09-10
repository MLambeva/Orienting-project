package com.orienting.common.repository;

import com.orienting.common.entity.CompetitionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


@Repository
public interface CompetitionRepository extends JpaRepository<CompetitionEntity, Integer> {
    List<CompetitionEntity> findCompetitionByDate(LocalDate date);
    CompetitionEntity findCompetitionByCompId(Integer compId);
}
