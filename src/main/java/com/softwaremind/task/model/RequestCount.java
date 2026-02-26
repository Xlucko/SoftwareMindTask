package com.softwaremind.task.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class RequestCount {

    public RequestCount(String method, String path, Long count) {
        this.method = method;
        this.path = path;
        this.count = count;
    }

    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private Long id;

    private String method;
    private String path;
    private Long count;
}
