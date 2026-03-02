package com.softwaremind.task.service;

import com.softwaremind.task.controller.search.ReservationSearchParams;
import com.softwaremind.task.dto.commands.ReservationCreateCommand;
import com.softwaremind.task.dto.commands.ReservationUpdateCommand;
import com.softwaremind.task.model.Reservation;
import com.softwaremind.task.model.SittingTable;
import com.softwaremind.task.repository.ReservationRepository;
import com.softwaremind.task.repository.SittingTableRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ReservationServiceTest {

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    SittingTableRepository tableRepository;

    @Autowired
    ReservationService reservationService;

    @Test
    void ifReservationIsPresent_FindOneById() {
        //given:
        SittingTable table = tableRepository.save(new SittingTable("test-res-table-1", 3));
        LocalDateTime start = LocalDate.now().plusDays(7).atTime(16, 30);
        Reservation reservation = reservationRepository.save(
                new Reservation(start, start.plusHours(1L), "Anonim", 2, table));

        //when:
        var foundReservation = reservationService.getReservationById(reservation.getId());

        //then:
        assertEquals(reservation.getId(), foundReservation.id());
        assertEquals(start.toLocalDate(), foundReservation.date());
        assertEquals(LocalTime.from(start), foundReservation.time());
        assertEquals(Duration.ofHours(1L), foundReservation.duration());
        assertEquals("Anonim", foundReservation.name());
        assertEquals(2, foundReservation.count());
    }

    @Test
    void whenSearchingReservationByDate_returnAllReservationAtDate() {
        //given:
        SittingTable table1 = tableRepository.save(new SittingTable("test-res-table-1", 3));
        SittingTable table2 = tableRepository.save(new SittingTable("test-res-table-2", 4));
        SittingTable table3 = tableRepository.save(new SittingTable("test-res-table-3", 2));

        LocalDate futureDate = LocalDate.now().plusDays(10);
        reservationRepository.save(
                new Reservation(futureDate.atTime(16, 0), futureDate.atTime(18, 0),
                        "Anthon", 3, table1));
        reservationRepository.save(
                new Reservation(futureDate.plusDays(7).atTime(16, 0),
                        futureDate.plusDays(7).atTime(18, 0),
                        "Anthon", 3, table1));
        reservationRepository.save(
                new Reservation(futureDate.atTime(16, 30), futureDate.atTime(19, 0),
                        "Beth", 2, table3));
        reservationRepository.save(
                new Reservation(futureDate.minusDays(1L).atTime(16, 0),
                        futureDate.minusDays(1L).atTime(18, 0),
                        "Chad", 4, table2));

        //when:
        var searchQuery = new ReservationSearchParams(futureDate, null, null, null);
        var foundReservations = reservationService.searchReservations(searchQuery, Pageable.unpaged());

        //then:
        assertEquals(2, foundReservations.size());
        foundReservations.forEach(reservationDTO -> assertEquals(futureDate, reservationDTO.date()));
    }

    @Test
    void whenCreatingReservation_returnAndAssignAvailableTable() {
        //given:
        LocalDate reservationDate = LocalDate.now().plusDays(5L);
        LocalTime reservationTime = LocalTime.of(18, 15);

        tableRepository.save(new SittingTable("test-res-table-1", 2));
        tableRepository.save(new SittingTable("test-res-table-2", 4));
        var tableOccupied = tableRepository.save(new SittingTable("test-res-table-occupied", 3));
        tableRepository.save(new SittingTable("test-res-table-3", 2));


        reservationRepository.save(new Reservation(
                reservationDate.atTime(reservationTime).minusMinutes(30),
                reservationDate.atTime(reservationTime).plusHours(1L),
                "Eleph", 3, tableOccupied
        ));

        //when:
        var command = new ReservationCreateCommand(
                reservationDate, reservationTime, Duration.ofHours(2L),
                "Dora", 3, null);
        var returnedReservation = reservationService.createReservation(command);

        //then:
        assertEquals("Dora", returnedReservation.name());
        assertEquals(reservationDate, returnedReservation.date());
        assertEquals(3, returnedReservation.count());
        assertNotEquals("test-res-table-occupied", returnedReservation.table());

        var savedReservation = reservationRepository.getReferenceById(returnedReservation.id());
        assertEquals(savedReservation.getStart(), returnedReservation.date().atTime(returnedReservation.time()));
        assertNotNull(savedReservation.getTable());
        assertTrue(savedReservation.getTable().getSize() >= command.count());
    }

    @Test
    void whenUpdatingReservation_updateReservationInDb() {
        //given:
        LocalDate reservationDate = LocalDate.now().plusDays(5L);
        LocalTime reservationTime = LocalTime.of(18, 15);

        var table = tableRepository.save(new SittingTable("test-res-table-1", 4));
        var reservationId = reservationRepository.save(new Reservation(
                        reservationDate.atTime(reservationTime),
                        reservationDate.atTime(reservationTime).plusHours(2L),
                        "Fiona", 3, table
                ))
                .getId();

        //when:
        var command = new ReservationUpdateCommand(reservationDate, reservationTime, Duration.ofMinutes(90L),
                "Fiona", 2, null);
        reservationService.updateReservation(reservationId, command);

        //then:
        var updatedReservation = reservationRepository.getReferenceById(reservationId);
        assertEquals(reservationDate.atTime(reservationTime), updatedReservation.getStart());
        assertEquals(Duration.ofMinutes(90L), Duration.between(updatedReservation.getStart(), updatedReservation.getEnd()));
        assertEquals(2, updatedReservation.getCount());
        assertEquals("Fiona", updatedReservation.getName());
    }

    @Test
    void whenUpsizingReservation_reassignTable() {
        //given:
        LocalDateTime reservationStart = LocalDate.now().plusDays(5L).atTime(18, 15);
        LocalDateTime reservationEnd = reservationStart.plusHours(2L);

        var table1 = tableRepository.save(new SittingTable("test-res-table-1", 2));
        tableRepository.save(new SittingTable("test-res-table-2", 4));
        tableRepository.save(new SittingTable("test-res-table-3", 3));
        var reservationId = reservationRepository.save(new Reservation(
                        reservationStart,
                        reservationEnd,
                        "Fiona", 2, table1
                ))
                .getId();

        //when:
        var command = new ReservationUpdateCommand(null, null, null,
                null, 3, null);
        reservationService.updateReservation(reservationId, command);

        //then:
        var updatedReservation = reservationRepository.getReferenceById(reservationId);
        assertEquals(3, updatedReservation.getCount());
        assertEquals("Fiona", updatedReservation.getName());
        assertNotEquals(table1, updatedReservation.getTable());
        assertEquals(3, updatedReservation.getTable().getSize());
        assertEquals(reservationStart, updatedReservation.getStart());
        assertEquals(reservationEnd, updatedReservation.getEnd());
    }

    @Test
    void whenDeletingReservation_reservationIsDeletedFromDb() {
        //given:
        LocalDate reservationDate = LocalDate.now().plusDays(5L);
        LocalTime reservationTime = LocalTime.of(18, 15);

        var table = tableRepository.save(new SittingTable("test-res-table-1", 4));
        var reservationId = reservationRepository.save(new Reservation(
                        reservationDate.atTime(reservationTime),
                        reservationDate.atTime(reservationTime).plusHours(2L),
                        "Fiona", 3, table
                ))
                .getId();

        //when:
        reservationService.deleteReservation(reservationId);

        //then:
        var reservationOptional = reservationRepository.findById(reservationId);
        assertTrue(reservationOptional.isEmpty());
    }
}