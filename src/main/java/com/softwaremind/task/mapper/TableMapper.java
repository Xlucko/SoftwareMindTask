package com.softwaremind.task.mapper;

import com.softwaremind.task.dto.TableDTO;
import com.softwaremind.task.model.SittingTable;
import org.springframework.stereotype.Component;

@Component
public class TableMapper {
    public TableDTO toDto(SittingTable table) {
        return new TableDTO(table.getId(), table.getCode(), table.getSize());
    }
}
