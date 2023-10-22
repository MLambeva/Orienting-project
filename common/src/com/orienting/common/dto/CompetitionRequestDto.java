package com.orienting.common.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class CompetitionRequestDto {
    private Integer compId;
    private String name;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate date;
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime time;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate deadline;
    private String location;
    private String coordinates;
    private Set<CompetitorDto> users;
}