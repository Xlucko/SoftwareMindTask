package com.softwaremind.task.service;

import com.softwaremind.task.controller.filter.ReservationFilterRequest;
import com.softwaremind.task.dto.ReservationCreateOrUpdateCommand;
import com.softwaremind.task.dto.ReservationDTO;
import com.softwaremind.task.exception.NoTableFoundException;
import com.softwaremind.task.exception.TargetTableNotAvailableException;
import com.softwaremind.task.mapper.ReservationMapper;
import com.softwaremind.task.model.Reservation;
import com.softwaremind.task.model.Reservation_;
import com.softwaremind.task.model.SittingTable;
import com.softwaremind.task.model.SittingTable_;
import com.softwaremind.task.repository.ReservationRepository;
import com.softwaremind.task.repository.SittingTableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Comparator;
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

    public List<ReservationDTO> searchReservations(ReservationFilterRequest filterRequest, Pageable pageable) {

        Specification<Reservation> spec = Specification.where((root, query, cb) -> cb.conjunction());

        if (filterRequest.date() != null) {

            if (filterRequest.time() != null) {
                LocalDateTime start = filterRequest.date().atTime(filterRequest.time());
                spec = spec.and((root, query, cb) ->
                        cb.equal(root.get(Reservation_.START), start));
            } else {
                LocalDateTime start = filterRequest.date().atStartOfDay();
                LocalDateTime end = start.plusDays(1L);
                spec = spec.and((root, query, cb) ->
                        cb.between(root.get(Reservation_.START), start, end));
            }
        }

        if (StringUtils.hasText(filterRequest.table())) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get(Reservation_.TABLE).get(SittingTable_.CODE), filterRequest.table()));
        }

        if (StringUtils.hasText(filterRequest.name())) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get(Reservation_.NAME), filterRequest.name()));
        }

        return reservationRepository.findAll(spec, pageable).stream().map(mapper::toDTO).toList();
    }

    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }

    public ReservationDTO createReservation(ReservationCreateOrUpdateCommand createCommand) {
        LocalDateTime start = createCommand.date().atTime(createCommand.time());
        LocalDateTime end = start.plus(createCommand.duration());

        List<SittingTable> freeTables = sittingTableRepository.availableTables(start, end, createCommand.count());
        SittingTable table;
        if (StringUtils.hasText(createCommand.table())) {
            if (CollectionUtils.isEmpty(freeTables)) {
                throw new TargetTableNotAvailableException(freeTables);
            } else {
                table = freeTables.stream().filter(t -> t.getCode().equals(createCommand.table()))
                        .findFirst()
                        .orElseThrow(() -> new TargetTableNotAvailableException(freeTables));
            }
        } else {
            if (freeTables.isEmpty()) {
                throw new NoTableFoundException();
            } else {
                freeTables.sort(Comparator.comparingInt(SittingTable::getSize));
                table = freeTables.getFirst();
            }

        }
        Reservation reservation = new Reservation();
        reservation.setName(createCommand.name());
        reservation.setCount(createCommand.count());
        reservation.setStart(start);
        reservation.setEnd(end);
        reservation.setTable(table);

        reservation = reservationRepository.saveAndFlush(reservation);
        return mapper.toDTO(reservation);
    }

    public void updateReservation(Long id, ReservationCreateOrUpdateCommand command) {
        Reservation reservation = reservationRepository.getReferenceById(id);

        LocalDateTime start = command.date().atTime(command.time());
        reservation.setStart(start);
        reservation.setEnd(start.plus(command.duration()));
        reservation.setName(command.name());
        reservation.setCount(command.count());

        reservationRepository.save(reservation);
    }
}
