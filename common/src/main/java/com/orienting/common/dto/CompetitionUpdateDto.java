package com.orienting.common.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Future;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Getter
@Setter
@NoArgsConstructor
public class CompetitionUpdateDto {
    private String name;
    @JsonFormat(pattern = "dd-MM-yyyy")
    @Future
    private LocalDate date;
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime time;
    @JsonFormat(pattern = "dd-MM-yyyy")
    //@FutureOrPresent
    private LocalDate deadline;
    private String location;
    private String coordinates;
    @JsonIgnore
    private Logger logger = LoggerFactory.getLogger(CompetitionUpdateDto.class);

    public CompetitionUpdateDto(String name, String date, String time, String deadline, String location, String coordinates) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        this.name = (name != null && !name.isEmpty() ?  name.replaceAll("%20", " ") : null);
        try {
            this.date = (date != null && !date.isEmpty()) ? LocalDate.parse(date, dateFormatter) : null;
            this.time = (time != null && !time.isEmpty()) ? LocalTime.parse(time, timeFormatter) : null;
            this.deadline = (deadline != null && !deadline.isEmpty()) ? LocalDate.parse(deadline, dateFormatter) : null;
        }catch (DateTimeParseException e) {
            logger.error("Invalid formats!");
        }
        this.location = (location != null && !location.isEmpty() ?  location.replaceAll("%20", " ") : null);
        this.coordinates = (coordinates != null && !coordinates.isEmpty()) ? coordinates : null;
    }
}
