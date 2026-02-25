package com.softwaremind.task.configuration;

import com.softwaremind.task.model.Reservation;
import com.softwaremind.task.model.SittingTable;
import com.softwaremind.task.repository.ReservationRepository;
import com.softwaremind.task.repository.SittingTableRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@Profile("dev")
@RequiredArgsConstructor
public class DbInitialData {

    private final SittingTableRepository tableRepository;
    private final ReservationRepository reservationRepository;

    @PostConstruct
    private void addInitialTables() {
        var table1front = tableRepository.save(new SittingTable("Front-1", 4));
        tableRepository.save(new SittingTable("Front-2", 2));
        tableRepository.save(new SittingTable("Front-3", 2));
        tableRepository.save(new SittingTable("Back-1", 4));
        tableRepository.save(new SittingTable("Back-2", 6));
        tableRepository.save(new SittingTable("Back-3", 6));

        LocalDateTime start1 = LocalDate.now().plusDays(1L).atTime(14, 0);
        reservationRepository.save(new Reservation(start1, start1.plusHours(2L), "Thomas", 2, table1front));

        LocalDateTime start2 = LocalDate.now().plusDays(1L).atTime(16, 0);
        reservationRepository.save(new Reservation(start2, start2.plusHours(2L), "Sarah", 2, table1front));

        LocalDateTime start3 = LocalDate.now().plusDays(1L).atTime(20, 0);
        reservationRepository.save(new Reservation(start3, start3.plusHours(2L), "Max", 1, table1front));
    }

}
