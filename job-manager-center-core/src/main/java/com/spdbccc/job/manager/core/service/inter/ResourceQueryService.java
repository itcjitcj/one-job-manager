package com.spdbccc.job.manager.core.service.inter;

import com.spdbccc.job.manager.core.common.entity.job.ParamsEntity;
import com.spdbccc.job.manager.core.common.pojo.YarnResourceInfo;

public interface ResourceQueryService {

    /**
     * 用于查询yarn的资源数 cpu memory task数
     * @param paramsEntity
     * @return
     */
    public YarnResourceInfo getYarnResource(ParamsEntity paramsEntity);
}
