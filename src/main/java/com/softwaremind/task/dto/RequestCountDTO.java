package com.softwaremind.task.dto;

public record RequestCountDTO(String method,
                              String path,
                              Long count) {
}
