package com.softwaremind.task.controller;

import com.softwaremind.task.controller.filter.TableFilterRequest;
import com.softwaremind.task.dto.TableCreateOrDeleteCommand;
import com.softwaremind.task.dto.TableDTO;
import com.softwaremind.task.service.TableService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/table")
public class TableController {

    private final TableService tableService;

    @GetMapping
    public List<TableDTO> searchTables(@ModelAttribute TableFilterRequest filterRequest, Pageable pageable) {
        return tableService.search(filterRequest, pageable);
    }

    @GetMapping("/{id}")
    public TableDTO getTable(@PathVariable("id") Long id) {
        return tableService.getTable(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public TableDTO createTable(@RequestBody TableCreateOrDeleteCommand command) {
        return tableService.createTable(command);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void updateTable(@PathVariable("id") Long id, @RequestBody TableCreateOrDeleteCommand command) {
        tableService.updateTable(id, command);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteTable(@PathVariable("id") Long id) {
        tableService.deleteTable(id);
    }
}
