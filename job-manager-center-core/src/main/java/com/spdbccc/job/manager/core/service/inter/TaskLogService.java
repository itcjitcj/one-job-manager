package com.spdbccc.job.manager.core.service.inter;

import com.spdbccc.job.manager.core.common.entity.job.LogEntity;

public interface TaskLogService {

    //读取对应文件的日志
    public String taskLogRead(LogEntity logEntity);

    //删除对应文件或目录的日志
    public String deleteLog(String logLocation);

    //清除日志目录下所有修改日期小于time的文件
    public String deleteLogTimeRange(String logLocation,long time);
    //如果日志目录下文件大于disk阈值，删除最早修改日期数据，直到disk阈值以内
    public String deleteLogDiskRange(String logLocation,long diskRange);

}
