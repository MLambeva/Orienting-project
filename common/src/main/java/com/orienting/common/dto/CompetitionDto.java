package com.orienting.common.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalTime;


@Getter
@Setter
public class CompetitionDto {
    private Integer compId;
    @NotBlank(message = "Name is mandatory!")
    private String name;
    @JsonFormat(pattern = "dd-MM-yyyy")
    @Future
    private LocalDate date;
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime time;
    private String location;
    private String coordinates;

    public CompetitionDto() {}
}
