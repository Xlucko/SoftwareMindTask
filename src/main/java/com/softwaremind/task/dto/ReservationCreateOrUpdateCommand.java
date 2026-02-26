package com.softwaremind.task.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

@NoArgsConstructor
@Getter
public class ReservationCreateOrUpdateCommand {
    private LocalDate date;
    private LocalTime time;
    private Duration duration;
    private String name;
    private Integer count;
}
