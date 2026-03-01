package com.softwaremind.task.repository;

import com.softwaremind.task.model.SittingTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface SittingTableRepository extends JpaRepository<SittingTable, Long>, JpaSpecificationExecutor<SittingTable> {


    @Query("""
            SELECT t FROM SittingTable t
            WHERE
                t.size >= ?3
                AND t.id NOT IN (
                    SELECT r.table.id FROM Reservation r
                    WHERE (r.end > ?1 AND r.start < ?2)
                )
            """)
    List<SittingTable> availableTables(LocalDateTime start, LocalDateTime end, Integer size);

    boolean existsByCode(String code);
}
