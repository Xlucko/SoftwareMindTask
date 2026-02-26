package com.softwaremind.task.controller;

import com.softwaremind.task.dto.ReservationCreateOrUpdateCommand;
import com.softwaremind.task.dto.ReservationDTO;
import com.softwaremind.task.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    @GetMapping("/{id}")
    public ReservationDTO getReservationById(@PathVariable("id") Long id) {
        return reservationService.getReservationById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReservation(@PathVariable("id") Long id) {
        reservationService.deleteReservation(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateReservation(@PathVariable("id") Long id, @Valid @RequestBody ReservationCreateOrUpdateCommand command) {
        reservationService.updateReservation(id, command);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationDTO createReservation(@Valid @RequestBody ReservationCreateOrUpdateCommand createCommand) {
        return reservationService.createReservation(createCommand);
    }


}
