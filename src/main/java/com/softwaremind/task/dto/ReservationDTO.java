package com.softwaremind.task.dto;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

public record ReservationDTO(
        Long id,
        LocalDate date,
        LocalTime time,
        Duration duration,
        String name,
        Integer count,
        String table
) {
}
