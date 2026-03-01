package com.softwaremind.task.controller;

import com.softwaremind.task.controller.filter.RequestFilterRequest;
import com.softwaremind.task.dto.RequestCountDTO;
import com.softwaremind.task.service.RequestCountService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/request")
public class RequestCountController {

    private final RequestCountService requestCountService;

    @GetMapping
    public List<RequestCountDTO> getAll(@ModelAttribute RequestFilterRequest filterRequest, Pageable pageable) {
        return requestCountService.getAllCounts(filterRequest, pageable);
    }
}
