package com.softwaremind.task.service;

import com.softwaremind.task.controller.search.ReservationSearchParams;
import com.softwaremind.task.dto.ReservationDTO;
import com.softwaremind.task.dto.commands.ReservationCreateCommand;
import com.softwaremind.task.dto.commands.ReservationUpdateCommand;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.time.LocalDate;
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

    public List<ReservationDTO> searchReservations(ReservationSearchParams filterRequest, Pageable pageable) {

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

    @Transactional
    public ReservationDTO createReservation(ReservationCreateCommand createCommand) {
        LocalDateTime start = createCommand.date().atTime(createCommand.time());
        LocalDateTime end = start.plus(createCommand.duration());

        List<SittingTable> freeTables = sittingTableRepository.availableTables(start, end, createCommand.count());
        SittingTable table = StringUtils.hasText(createCommand.table()) ?
                findDesiredTable(freeTables, createCommand.table()) : findBestTable(freeTables);
        Reservation reservation = new Reservation();
        reservation.setName(createCommand.name());
        reservation.setCount(createCommand.count());
        reservation.setStart(start);
        reservation.setEnd(end);
        reservation.setTable(table);

        reservation = reservationRepository.save(reservation);
        return mapper.toDTO(reservation);
    }

    @Transactional
    public void updateReservation(Long id, ReservationUpdateCommand command) {
        Reservation reservation = reservationRepository.getReferenceById(id);

        LocalDate newDate = command.date() != null ?
                command.date() : reservation.getStart().toLocalDate();
        LocalDateTime newStart = command.time() != null ?
                newDate.atTime(command.time()) : newDate.atTime(reservation.getStart().toLocalTime());
        LocalDateTime newEnd = command.duration() != null ?
                newStart.plus(command.duration()) :
                newStart.plus(Duration.between(reservation.getStart(), reservation.getEnd()));
        Integer newCount = command.count() != null ?
                command.count() : reservation.getCount();
        SittingTable table = reservation.getTable();

        if (mightRequireTableChange(reservation, newCount, newStart, newEnd)) {
            List<SittingTable> availableTables = sittingTableRepository.availableTablesIgnoringReservationById(newStart, newEnd, newCount, id);
            if (StringUtils.hasText(command.table())) {
                table = findDesiredTable(availableTables, command.table());
            } else if (!availableTables.contains(table)) {
                table = findBestTable(availableTables);
            }
        }

        if (StringUtils.hasText(command.name())) {
            reservation.setName(command.name());
        }
        reservation.setStart(newStart);
        reservation.setEnd(newEnd);
        reservation.setCount(newCount);
        reservation.setTable(table);

        reservationRepository.save(reservation);
    }

    private SittingTable findDesiredTable(List<SittingTable> freeTables, String desiredTable) {
        if (CollectionUtils.isEmpty(freeTables)) {
            throw new TargetTableNotAvailableException(freeTables);
        } else {
            return freeTables.stream().filter(t -> t.getCode().equals(desiredTable))
                    .findFirst()
                    .orElseThrow(() -> new TargetTableNotAvailableException(freeTables));
        }
    }

    private SittingTable findBestTable(List<SittingTable> freeTables) {
        if (freeTables.isEmpty()) {
            throw new NoTableFoundException();
        } else {
            freeTables.sort(Comparator.comparingInt(SittingTable::getSize));
            return freeTables.getFirst();
        }
    }

    private boolean mightRequireTableChange(Reservation reservation, Integer newCount, LocalDateTime newStart, LocalDateTime newEnd) {
        if (newCount > reservation.getTable().getSize()) {
            return true;
        }
        if (newStart != reservation.getStart()) {
            return true;
        }
        if (newEnd != reservation.getEnd()) {
            return true;
        }
        return false;
    }
}
