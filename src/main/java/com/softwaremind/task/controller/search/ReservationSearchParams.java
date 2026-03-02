package com.softwaremind.task.controller.search;

import java.time.LocalDate;
import java.time.LocalTime;

public record ReservationSearchParams(
        LocalDate date,
        LocalTime time,
        String name,
        String table
) {
}
