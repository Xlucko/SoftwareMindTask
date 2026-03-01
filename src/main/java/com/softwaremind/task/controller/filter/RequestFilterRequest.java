package com.softwaremind.task.controller.filter;

public record RequestFilterRequest(
        String method,
        String path,
        Long minCount,
        Long maxCount
) {
}
