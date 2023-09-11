package com.orienting.common.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.orienting.common.entity.UserEntity;
import jakarta.validation.constraints.Future;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

@Getter
@Setter
public class CompetitionRequestDto {
    private Integer compId;
    private String name;
    @JsonFormat(pattern = "dd-MM-yyyy")
    @Future
    private LocalDate date;
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime time;
    private String location;
    private String coordinates;
    private Set<CompetitorDto> users;

    public CompetitionRequestDto() {}
}