package com.softwaremind.task.controller.filter;

import java.time.LocalDate;
import java.time.LocalTime;

public record ReservationFilterRequest(
        LocalDate date,
        LocalTime time,
        String name,
        String table
) {
}
