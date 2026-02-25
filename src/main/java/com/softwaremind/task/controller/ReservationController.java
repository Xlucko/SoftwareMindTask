package com.softwaremind.task.controller;

import com.softwaremind.task.dto.ReservationCreateCommand;
import com.softwaremind.task.dto.ReservationDTO;
import com.softwaremind.task.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/reservation")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping
    public List<ReservationDTO> getReservationsForDate(@RequestParam("date") LocalDate localDate) {
        return reservationService.getAllForDate(localDate);
    }

    @PostMapping
    public ReservationDTO createReservation(@RequestBody ReservationCreateCommand createCommand) {
        return reservationService.createReservation(createCommand);
    }


}
