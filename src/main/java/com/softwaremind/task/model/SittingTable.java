package com.softwaremind.task.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class SittingTable {

    public SittingTable(String code, Integer size) {
        this.code = code;
        this.size = size;
        this.reservations = new ArrayList<>();
    }

    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private Long id;

    private String code;
    private Integer size;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "table")
    private List<Reservation> reservations;
}
