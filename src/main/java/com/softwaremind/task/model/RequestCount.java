package com.softwaremind.task.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(indexes = @Index(columnList = "method, path", unique = true))
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
