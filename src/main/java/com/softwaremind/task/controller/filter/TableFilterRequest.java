package com.softwaremind.task.controller.filter;

public record TableFilterRequest(
        String code,
        Integer sizeMin,
        Integer sizeMax
) {

}
