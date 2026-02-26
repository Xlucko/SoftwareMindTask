package com.softwaremind.task.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class TableCreateOrDeleteCommand {
    private String code;
    private Integer size;
}
