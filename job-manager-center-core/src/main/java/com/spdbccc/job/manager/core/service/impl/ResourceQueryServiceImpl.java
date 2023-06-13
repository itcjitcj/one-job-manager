package com.spdbccc.job.manager.core.service.impl;

import com.spdbccc.job.manager.core.common.entity.job.ParamsEntity;
import com.spdbccc.job.manager.core.common.pojo.HttpYarnApp;
import com.spdbccc.job.manager.core.common.pojo.YarnResourceInfo;
import com.spdbccc.job.manager.core.service.inter.ResourceQueryService;
import com.spdbccc.job.manager.core.util.YarnApiUtils;

import java.util.List;

public class ResourceQueryServiceImpl implements ResourceQueryService {


    @Override
    public YarnResourceInfo getYarnResource(ParamsEntity paramsEntity) {
        List<HttpYarnApp> httpYarnAppList = YarnApiUtils.getAllApp(paramsEntity.getYarnUrl(), paramsEntity.getUser(),
                1, paramsEntity.getQueue(), false);
        YarnResourceInfo yarnResourceInfo = new YarnResourceInfo(paramsEntity.getCpuThreshold(),paramsEntity.getMemoryThreshold());
        if (httpYarnAppList == null || httpYarnAppList.isEmpty()) {
           return yarnResourceInfo;
        }else{
            yarnResourceInfo.setTasks(httpYarnAppList.size());
            Integer coreUse = yarnResourceInfo.getCoreUse();
            Integer memoryUse = yarnResourceInfo.getMemoryUse();
            for (HttpYarnApp httpYarnApp : httpYarnAppList) {
                Integer cores = httpYarnApp.getAllocatedVCores();
                Integer memory = httpYarnApp.getAllocatedMB();
                coreUse+=cores;
                memoryUse+=memory;
            }
            yarnResourceInfo.setCoreUse(coreUse);
            yarnResourceInfo.setMemoryUse(memoryUse);
            yarnResourceInfo.setCoreUsePer(Double.valueOf(coreUse)/paramsEntity.getCpuThreshold());
            yarnResourceInfo.setMemoryUsePer(Double.valueOf(memoryUse)/paramsEntity.getMemoryThreshold());
            return yarnResourceInfo;
        }

    }
}
