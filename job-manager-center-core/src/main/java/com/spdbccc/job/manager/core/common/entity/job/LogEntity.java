package com.spdbccc.job.manager.core.common.entity.job;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogEntity {
    private long offset;

    private String logLocation;

    private StringBuilder logInfo=new StringBuilder();

    public LogEntity(long offset, String logLocation) {
        this.offset = offset;
        this.logLocation = logLocation;
    }
}
