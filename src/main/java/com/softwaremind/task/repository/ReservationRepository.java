package com.softwaremind.task.repository;

import com.softwaremind.task.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    default List<Reservation> findByDate(LocalDate date) {
        return findByStartBetween(date.atStartOfDay(), date.plusDays(1).atStartOfDay());
    }

    List<Reservation> findByStartBetween(LocalDateTime start, LocalDateTime end);
}
