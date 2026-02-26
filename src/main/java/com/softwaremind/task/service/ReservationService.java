package com.softwaremind.task.service;

import com.softwaremind.task.dto.ReservationCreateOrUpdateCommand;
import com.softwaremind.task.dto.ReservationDTO;
import com.softwaremind.task.exception.NoTableFoundException;
import com.softwaremind.task.mapper.ReservationMapper;
import com.softwaremind.task.model.Reservation;
import com.softwaremind.task.repository.ReservationRepository;
import com.softwaremind.task.repository.SittingTableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final SittingTableRepository sittingTableRepository;
    private final ReservationMapper mapper;

    public ReservationDTO getReservationById(Long id) {
        return mapper.toDTO(reservationRepository.getReferenceById(id));
    }

    public List<ReservationDTO> getAllForDate(LocalDate localDate) {
        return reservationRepository.findByDate(localDate).stream().map(mapper::toDTO).toList();
    }

    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }

    public ReservationDTO createReservation(ReservationCreateOrUpdateCommand createCommand) {
        LocalDateTime start = createCommand.getDate().atTime(createCommand.getTime());
        LocalDateTime end = start.plus(createCommand.getDuration());

        var freeTables = sittingTableRepository.availableTables(start, end, createCommand.getCount());
        if (freeTables.isEmpty()) {
            throw new NoTableFoundException();
        }

        Reservation reservation = new Reservation();
        reservation.setName(createCommand.getName());
        reservation.setCount(createCommand.getCount());
        reservation.setStart(start);
        reservation.setEnd(end);
        reservation.setTable(freeTables.getFirst());

        reservation = reservationRepository.saveAndFlush(reservation);
        return mapper.toDTO(reservation);
    }

    public void updateReservation(Long id, ReservationCreateOrUpdateCommand command) {
        Reservation reservation = reservationRepository.getReferenceById(id);

        LocalDateTime start = command.getDate().atTime(command.getTime());
        reservation.setStart(start);
        reservation.setEnd(start.plus(command.getDuration()));
        reservation.setName(command.getName());
        reservation.setCount(command.getCount());

        reservationRepository.save(reservation);
    }
}
