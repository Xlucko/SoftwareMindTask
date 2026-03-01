package com.softwaremind.task.service;

import com.softwaremind.task.controller.filter.TableFilterRequest;
import com.softwaremind.task.dto.TableCreateOrDeleteCommand;
import com.softwaremind.task.dto.TableDTO;
import com.softwaremind.task.mapper.TableMapper;
import com.softwaremind.task.model.SittingTable;
import com.softwaremind.task.model.SittingTable_;
import com.softwaremind.task.repository.SittingTableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TableService {

    private final SittingTableRepository tableRepository;
    private final TableMapper tableMapper;


    public List<TableDTO> search(TableFilterRequest filterRequest, Pageable pageable) {
        Specification<SittingTable> spec = Specification.where((root, query, cb) -> cb.conjunction());

        if (StringUtils.hasText(filterRequest.code())) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get(SittingTable_.CODE), filterRequest.code()));
        }
        if (filterRequest.sizeMin() != null) {
            spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get(SittingTable_.SIZE), filterRequest.sizeMin()));
        }

        if (filterRequest.sizeMax() != null) {
            spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get(SittingTable_.SIZE), filterRequest.sizeMax()));
        }
        return tableRepository.findAll(spec, pageable).stream().map(tableMapper::toDto).toList();
    }

    public TableDTO getTable(long id) {
        return tableMapper.toDto(tableRepository.getReferenceById(id));
    }

    public TableDTO createTable(TableCreateOrDeleteCommand command) {
        checkUniqueCode(command.code());
        SittingTable table = new SittingTable(command.code(), command.size());
        return tableMapper.toDto(tableRepository.save(table));
    }

    public void updateTable(Long id, TableCreateOrDeleteCommand command) {
        checkUniqueCode(command.code());
        SittingTable table = tableRepository.getReferenceById(id);
        table.setSize(command.size());
        table.setCode(command.code());
        tableRepository.save(table);
    }

    public void deleteTable(Long id) {
        tableRepository.deleteById(id);
    }

    private void checkUniqueCode(String code) {
        if (tableRepository.existsByCode(code)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Code is not unique");
        }
    }
}
