package com.orienting.common.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompetitionDto {
    private Integer compId;
    @NotBlank(message = "Name is mandatory!")
    private String name;
    @JsonFormat(pattern = "dd-MM-yyyy")
    @Future
    private LocalDate date;
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime time;
    @JsonFormat(pattern = "dd-MM-yyyy")
    @FutureOrPresent
    private LocalDate deadline;
    private String location;
    private String coordinates;

    public CompetitionDto(String name, LocalDate date, LocalTime time, LocalDate deadline, String location, String coordinates) {
        this.name = name;
        this.date = date;
        this.time = time;
        this.deadline = deadline;
        this.location = location;
        this.coordinates = coordinates;
    }
}
