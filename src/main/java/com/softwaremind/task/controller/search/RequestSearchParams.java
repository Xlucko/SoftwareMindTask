package com.softwaremind.task.controller.search;

public record RequestSearchParams(
        String method,
        String path,
        Long minCount,
        Long maxCount
) {
}
