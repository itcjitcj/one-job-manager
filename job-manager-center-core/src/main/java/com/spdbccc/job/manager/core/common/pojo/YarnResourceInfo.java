package com.spdbccc.job.manager.core.common.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class YarnResourceInfo
{
    // cpu 使用 单位为core
    private Integer coreUse=0;
    //memory 使用 单位为mb
    private Integer memoryUse=0;
    private Integer tasks=0;
    private Double coreUsePer=0.0;
    private Double memoryUsePer=0.0;
    private Integer cpuThreshold;
    private Integer memoryThreshold;

    public YarnResourceInfo(Integer cpuThreshold, Integer memoryThreshold) {
        this.cpuThreshold = cpuThreshold;
        this.memoryThreshold = memoryThreshold;
    }
}
