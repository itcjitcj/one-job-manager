package com.spdbccc.job.manager.core.common.entity.job;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParamsEntity {
    //日志文件前缀 例如：/applog/airm/shell/
    private String logPrefix;
    private String node;
    private String user;
    private String password;
    private String yarnUrl;
    private String queue="default";
    private Integer cpuThreshold;
    private Integer memoryThreshold;

    public ParamsEntity(String logPrefix, String node, String user, String password, String yarnUrl, String queue) {
        this.logPrefix = logPrefix;
        this.node = node;
        this.user = user;
        this.password = password;
        this.yarnUrl = yarnUrl;
        this.queue = queue;
    }

    public ParamsEntity(String user,String yarnUrl, Integer cpuThreshold, Integer memoryThreshold) {
        this.user = user;
        this.yarnUrl = yarnUrl;
        this.cpuThreshold = cpuThreshold;
        this.memoryThreshold = memoryThreshold;
    }

}
