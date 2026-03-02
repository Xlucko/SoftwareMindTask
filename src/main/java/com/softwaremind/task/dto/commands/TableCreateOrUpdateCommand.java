package com.softwaremind.task.dto.commands;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record TableCreateOrUpdateCommand(
        @NotBlank String code,
        @Positive Integer size
) {
}
