package com.softwaremind.task.dto.commands;


import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

public record ReservationUpdateCommand(
        LocalDate date,
        LocalTime time,
        Duration duration,
        String name,
        Integer count,
        String table) {
}
