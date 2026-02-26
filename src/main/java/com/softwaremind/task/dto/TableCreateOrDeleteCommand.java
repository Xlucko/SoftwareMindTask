package com.softwaremind.task.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record TableCreateOrDeleteCommand(
        @NotBlank String code,
        @Positive Integer size
) {
}
