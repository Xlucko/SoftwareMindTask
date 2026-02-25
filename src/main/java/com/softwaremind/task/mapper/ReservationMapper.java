package com.softwaremind.task.mapper;

import com.softwaremind.task.dto.ReservationDTO;
import com.softwaremind.task.model.Reservation;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class ReservationMapper {

    public ReservationDTO toDTO(Reservation reservation) {
        return new ReservationDTO(
                reservation.getId(),
                reservation.getStart().toLocalDate(),
                reservation.getStart().toLocalTime(),
                Duration.between(reservation.getStart(), reservation.getEnd()),
                reservation.getName(),
                reservation.getCount(),
                reservation.getTable().getCode()
        );
    }
}
