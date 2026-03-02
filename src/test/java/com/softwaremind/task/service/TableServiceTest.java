package com.softwaremind.task.service;


import com.softwaremind.task.controller.search.TableSearchParams;
import com.softwaremind.task.dto.TableDTO;
import com.softwaremind.task.dto.commands.TableCreateOrUpdateCommand;
import com.softwaremind.task.model.SittingTable;
import com.softwaremind.task.model.SittingTable_;
import com.softwaremind.task.repository.SittingTableRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class TableServiceTest {

    @Autowired
    private SittingTableRepository tableRepository;

    @Autowired
    private TableService tableService;

    @Test
    void ifTableIsPresent_FindOneById() {
        //given:
        SittingTable table1 = new SittingTable("test-table-1", 3);
        Long savedTableId = tableRepository.save(table1).getId();

        //when:
        TableDTO foundTable = tableService.getTable(savedTableId);

        //then:
        assertEquals("test-table-1", foundTable.code());
        assertEquals(3, foundTable.size());
    }

    @Test
    void whenSearchingTablesBySize_returnFilteredTables() {
        //given:
        SittingTable table1 = new SittingTable("test-table-1", 4);
        SittingTable table2 = new SittingTable("test-table-2", 2);
        SittingTable table3 = new SittingTable("test-table-3", 3);
        SittingTable table4 = new SittingTable("test-table-4", 6);

        tableRepository.save(table1);
        tableRepository.save(table2);
        tableRepository.save(table3);
        tableRepository.save(table4);

        //when:
        TableSearchParams tableSearchParams = new TableSearchParams("", 3, 4);
        var foundTables = tableService.search(tableSearchParams, Pageable.unpaged());

        //then
        assertEquals(2, foundTables.size());
        foundTables.forEach(tableDTO -> assertTrue(tableDTO.size() >= 3 && tableDTO.size() <= 4));
    }

    @Test
    void whenCreatingTable_returnSavedTable() {
        //when:
        TableCreateOrUpdateCommand command = new TableCreateOrUpdateCommand("test-table-A", 3);
        var createdTable = tableService.createTable(command);
        var savedTable = tableRepository.findOne((from, cb) -> cb.equal(from.get(SittingTable_.CODE), "test-table-A"));

        //then:
        assertEquals("test-table-A", createdTable.code());
        assertEquals(3, createdTable.size());

        assertTrue(savedTable.isPresent());
        assertEquals("test-table-A", savedTable.get().getCode());
        assertEquals(3, savedTable.get().getSize());
    }

    @Test
    void whenCreatingTableWithNonUniqueCode_throwResponseStatusException() {
        //given
        tableRepository.save(new SittingTable("test-table-A", 2));
        TableCreateOrUpdateCommand command = new TableCreateOrUpdateCommand("test-table-A", 3);

        //then:
        assertThrows(ResponseStatusException.class, () -> tableService.createTable(command));
    }

    @Test
    void whenUpdatingTable_updateTableInDb() {
        //given:
        SittingTable table1 = new SittingTable("test-table-1", 4);
        SittingTable table2 = new SittingTable("test-table-2", 2);

        var tableToChangeId = tableRepository.save(table1).getId();
        tableRepository.save(table2);

        //when:
        TableCreateOrUpdateCommand command = new TableCreateOrUpdateCommand("test-table-A", 3);
        tableService.updateTable(tableToChangeId, command);

        //then
        var updatedTable = tableRepository.getReferenceById(tableToChangeId);
        assertEquals("test-table-A", updatedTable.getCode());
        assertEquals(3, updatedTable.getSize());
    }

    @Test
    void whenDeletingTable_tableIsDeletedFromDb() {
        //given:
        SittingTable table1 = new SittingTable("test-table-1", 4);
        SittingTable table2 = new SittingTable("test-table-2", 2);

        var tableToDeleteId = tableRepository.save(table1).getId();
        tableRepository.save(table2);

        //when:
        tableService.deleteTable(tableToDeleteId);

        //then
        assertFalse(tableRepository.existsByCode("test-table-1"));
        assertTrue(tableRepository.existsByCode("test-table-2"));
    }

}
