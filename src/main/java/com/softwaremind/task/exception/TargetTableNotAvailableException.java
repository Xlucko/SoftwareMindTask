package com.softwaremind.task.exception;

import com.softwaremind.task.model.SittingTable;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.web.ErrorResponseException;

import java.util.List;
import java.util.StringJoiner;

public class TargetTableNotAvailableException extends ErrorResponseException {
    public TargetTableNotAvailableException(List<SittingTable> availableTables) {
        super(HttpStatus.CONFLICT);
        String detail = CollectionUtils.isEmpty(availableTables) ?
                "Desired table and others are not available" :
                "Desired table is not available, other tables are available: ";
        StringJoiner stringJoiner = new StringJoiner(", ", detail, ".");
        for (SittingTable availableTable : availableTables) {
            stringJoiner.add(availableTable.getCode());
        }
        setDetail(stringJoiner.toString());
    }
}
