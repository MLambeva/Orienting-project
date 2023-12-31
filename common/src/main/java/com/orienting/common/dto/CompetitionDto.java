package com.orienting.common.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CompetitionDto {
    private Integer compId;
    @NotBlank(message = "Name is mandatory!")
    private String name;
    @JsonFormat(pattern = "dd-MM-yyyy")
    @Future
    @NotNull(message = "Date is mandatory!")
    private LocalDate date;
    @JsonFormat(pattern = "HH:mm:ss")
    @NotNull(message = "Time is mandatory!")
    private LocalTime time;
    @JsonFormat(pattern = "dd-MM-yyyy")
    @FutureOrPresent
    @NotNull(message = "Deadline is mandatory!")
    private LocalDate deadline;
    private String location;
    private String coordinates;
    @JsonIgnore
    private Logger logger = LoggerFactory.getLogger(CompetitionDto.class);

    public CompetitionDto(String name, LocalDate date, LocalTime time, LocalDate deadline, String location, String coordinates) {
        this.name = name;
        this.date = date;
        this.time = time;
        this.deadline = deadline;
        this.location = location;
        this.coordinates = coordinates;
    }
    public CompetitionDto(String name, String date, String time, String deadline, String location, String coordinates) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        this.name = (name != null && !name.isEmpty() ?  name.replaceAll("%20", " ") : null);
        try {
            this.date = LocalDate.parse(date, dateFormatter);
            this.time = LocalTime.parse(time, timeFormatter);
            this.deadline = LocalDate.parse(deadline, dateFormatter);
        }catch (DateTimeParseException e) {
            logger.error("Invalid formats!");
        }
        this.location = (location != null && !location.isEmpty() ?  location.replaceAll("%20", " ") : null);
        this.coordinates = coordinates;
    }
}
