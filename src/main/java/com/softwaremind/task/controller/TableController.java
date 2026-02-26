package com.softwaremind.task.controller;

import com.softwaremind.task.dto.TableCreateOrDeleteCommand;
import com.softwaremind.task.dto.TableDTO;
import com.softwaremind.task.service.TableService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/table")
public class TableController {

    private final TableService tableService;

    @GetMapping
    public List<TableDTO> getAllTables() {
        return tableService.getAllTables();
    }

    @GetMapping("/{id}")
    public TableDTO getTable(@PathVariable("id") Long id) {
        return tableService.getTable(id);
    }

    @PostMapping
    public TableDTO createTable(@RequestBody TableCreateOrDeleteCommand command) {
        return tableService.createTable(command);
    }

    @PutMapping("/{id}")
    public void updateTable(@PathVariable("id") Long id, @RequestBody TableCreateOrDeleteCommand command) {
        tableService.updateTable(id, command);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTable(@PathVariable("id") Long id) {
        tableService.deleteTable(id);
    }
}
