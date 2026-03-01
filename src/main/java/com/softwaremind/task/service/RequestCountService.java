package com.softwaremind.task.service;

import com.softwaremind.task.controller.filter.RequestFilterRequest;
import com.softwaremind.task.dto.RequestCountDTO;
import com.softwaremind.task.model.RequestCount;
import com.softwaremind.task.model.RequestCount_;
import com.softwaremind.task.repository.RequestCountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RequestCountService {

    private final RequestCountRepository countRepository;

    public List<RequestCountDTO> getAllCounts(RequestFilterRequest filterRequest, Pageable pageable) {

        Specification<RequestCount> spec = prepareSpec(filterRequest);

        return countRepository.findAll(spec, pageable).stream()
                .map(requestCount -> new RequestCountDTO(requestCount.getMethod(), requestCount.getPath(), requestCount.getCount()))
                .toList();

    }

    private Specification<RequestCount> prepareSpec(RequestFilterRequest filterRequest) {
        Specification<RequestCount> spec = Specification.where((_, _, cb) -> cb.conjunction());

        if (StringUtils.hasText(filterRequest.method())) {
            spec = spec.and((root, _, cb) -> cb.equal(root.get(RequestCount_.METHOD), filterRequest.method()));
        }

        if (StringUtils.hasText(filterRequest.path())) {
            spec = spec.and((root, _, cb) -> cb.like(root.get(RequestCount_.PATH), filterRequest.path()));
        }

        if (filterRequest.maxCount() != null) {
            spec = spec.and((root, _, cb) -> cb.lessThanOrEqualTo(root.get(RequestCount_.COUNT), filterRequest.maxCount()));
        }

        if (filterRequest.minCount() != null) {
            spec = spec.and((root, _, cb) -> cb.greaterThanOrEqualTo(root.get(RequestCount_.COUNT), filterRequest.minCount()));
        }
        return spec;
    }

}
