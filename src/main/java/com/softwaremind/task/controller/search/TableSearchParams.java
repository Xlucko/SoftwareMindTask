package com.softwaremind.task.controller.search;

public record TableSearchParams(
        String code,
        Integer sizeMin,
        Integer sizeMax
) {

}
