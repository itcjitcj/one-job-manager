package com.spdbccc.job.manager.core.service.inter;

import com.spdbccc.job.manager.core.common.entity.job.ParamsEntity;
import com.spdbccc.job.manager.core.common.entity.job.TaskEntity;

public interface ShellService {

    /**
     * 用于执行shell命令 可以通过ParamsEntity 是否设定node值来判断它是ssh执行 还是本地执行
     * @param taskEntity
     * @param paramsEntity
     */
    public void runShell(TaskEntity taskEntity, ParamsEntity paramsEntity);

    /**
     * 用于停止正在执行的shell任务
     * @param taskEntity
     * @param paramsEntity
     */
    public boolean stopShell(TaskEntity taskEntity, ParamsEntity paramsEntity);
}
