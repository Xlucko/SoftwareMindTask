package com.softwaremind.task.dto;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

public record ReservationCreateOrUpdateCommand(
        LocalDate date,
        LocalTime time,
        Duration duration,
        String name,
        Integer count
) {
}
