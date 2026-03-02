package com.softwaremind.task.configuration;

import com.softwaremind.task.configuration.filter.RequestCountLoggingFilter;
import com.softwaremind.task.repository.RequestCountRepository;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.AbstractRequestLoggingFilter;

@Configuration
public class RequestCountConfiguration {

    @Bean
    public FilterRegistrationBean<AbstractRequestLoggingFilter> requestCountFilter(RequestCountRepository countRepository) {
        FilterRegistrationBean<AbstractRequestLoggingFilter> requestCountFilter = new FilterRegistrationBean<>();
        requestCountFilter.setFilter(new RequestCountLoggingFilter(countRepository));
        requestCountFilter.addUrlPatterns("/table/*", "/reservation/*");
        return requestCountFilter;
    }
}
