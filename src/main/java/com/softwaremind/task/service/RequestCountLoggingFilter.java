package com.softwaremind.task.service;

import com.softwaremind.task.model.RequestCount;
import com.softwaremind.task.repository.RequestCountRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.AbstractRequestLoggingFilter;


@RequiredArgsConstructor
public class RequestCountLoggingFilter extends AbstractRequestLoggingFilter {

    private final RequestCountRepository countRepository;

    public void increment(String method, String path) {
        RequestCount requestCount = countRepository.findByMethodAndPath(method, path).orElseGet(() -> new RequestCount(method, path, 0L));
        requestCount.setCount(requestCount.getCount() + 1);
        countRepository.save(requestCount);
    }

    @Override
    protected void beforeRequest(HttpServletRequest request, String message) {
        String method = request.getMethod();
        String path = request.getServletPath();
        increment(method, path);
    }

    @Override
    protected void afterRequest(HttpServletRequest request, String message) {
        //Not used
    }
}
