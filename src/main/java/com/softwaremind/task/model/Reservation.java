package com.softwaremind.task.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Reservation {

    public Reservation(LocalDateTime start, LocalDateTime end, String name, Integer count, SittingTable table) {
        this.start = start;
        this.end = end;
        this.name = name;
        this.count = count;
        this.table = table;
    }

    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private Long id;

    private LocalDateTime start;

    @Column(name = "reservation_end")
    private LocalDateTime end;

    private String name;
    private Integer count;

    @ManyToOne(fetch = FetchType.LAZY)
    private SittingTable table;
}
