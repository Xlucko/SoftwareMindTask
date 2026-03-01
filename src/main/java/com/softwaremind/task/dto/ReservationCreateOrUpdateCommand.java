package com.softwaremind.task.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

public record ReservationCreateOrUpdateCommand(
        @NotNull LocalDate date,
        @NotNull  LocalTime time,
        @NotNull Duration duration,
        @NotBlank String name,
        @Positive Integer count,
        @Nullable String table
) {
}
