package com.softwaremind.task.dto;

public record TableCreateOrDeleteCommand(
        String code,
        Integer size
) {
}
