package com.softwaremind.task.controller;

import com.softwaremind.task.controller.filter.ReservationFilterRequest;
import com.softwaremind.task.dto.ReservationCreateOrUpdateCommand;
import com.softwaremind.task.dto.ReservationDTO;
import com.softwaremind.task.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservation")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping
    public List<ReservationDTO> searchReservations(@ModelAttribute ReservationFilterRequest filterRequest, Pageable pageable) {
        return reservationService.searchReservations(filterRequest, pageable);
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
    public void updateReservation(@PathVariable("id") Long id, @RequestBody ReservationCreateOrUpdateCommand command) {
        reservationService.updateReservation(id, command);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationDTO createReservation(@RequestBody ReservationCreateOrUpdateCommand createCommand) {
        return reservationService.createReservation(createCommand);
    }


}
