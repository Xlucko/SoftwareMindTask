package com.softwaremind.task.service;

import com.softwaremind.task.dto.TableCreateOrDeleteCommand;
import com.softwaremind.task.dto.TableDTO;
import com.softwaremind.task.mapper.TableMapper;
import com.softwaremind.task.model.SittingTable;
import com.softwaremind.task.repository.SittingTableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TableService {

    private final SittingTableRepository tableRepository;
    private final TableMapper tableMapper;



    public List<TableDTO> getAllTables() {
        return tableRepository.findAll().stream().map(tableMapper::toDto).toList();
    }

    public TableDTO getTable(long id) {
        return tableMapper.toDto(tableRepository.getReferenceById(id));
    }

    public TableDTO createTable(TableCreateOrDeleteCommand command) {
        SittingTable table = new SittingTable(command.getCode(), command.getSize());
        return tableMapper.toDto(tableRepository.save(table));
    }

    public void updateTable(Long id, TableCreateOrDeleteCommand command) {
        SittingTable table = tableRepository.getReferenceById(id);
        table.setSize(command.getSize());
        table.setCode(command.getCode());
        tableRepository.save(table);
    }

    public void deleteTable(Long id) {
        tableRepository.deleteById(id);
    }
}
