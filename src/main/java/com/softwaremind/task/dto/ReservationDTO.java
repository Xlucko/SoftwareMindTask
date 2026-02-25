package com.softwaremind.task.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ReservationDTO implements Serializable {

    private Long id;
    private LocalDate date;
    private LocalTime time;
    private Duration duration;
    private String name;
    private Integer count;
    private String table;
}
