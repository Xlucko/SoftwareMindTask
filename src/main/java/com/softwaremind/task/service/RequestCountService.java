package com.softwaremind.task.service;

import com.softwaremind.task.dto.RequestCountDTO;
import com.softwaremind.task.repository.RequestCountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RequestCountService {

    private final RequestCountRepository countRepository;

    public List<RequestCountDTO> getAllCounts() {
        return countRepository.findAll().stream()
                .map(requestCount -> new RequestCountDTO(requestCount.getMethod(), requestCount.getPath(), requestCount.getCount()))
                .toList();

    }

}
