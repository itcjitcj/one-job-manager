package com.spdbccc.job.manager.core.common.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HttpYarnApp {

    private String id;
    private String name;
    private String user;
    private String queue;
    private String state;
    private String finalStatus;
    private String trackingUrl;
    private String diagnostics;
    private String applicationType;
    private Long startedTime;
    private Long finishedTime;
    private Integer allocatedMB;
    private Integer allocatedVCores;

}
