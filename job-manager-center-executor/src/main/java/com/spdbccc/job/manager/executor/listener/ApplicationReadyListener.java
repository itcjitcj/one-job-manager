package com.spdbccc.job.manager.executor.listener;

import com.spdbccc.job.manager.core.common.entity.job.ParamsEntity;
import com.spdbccc.job.manager.core.common.entity.job.TaskEntity;
import com.spdbccc.job.manager.db.dao.JmcJobParamsMapper;
import com.spdbccc.job.manager.db.dao.JmcTaskInfoMapper;
import com.spdbccc.job.manager.db.entity.JmcJobParams;
import com.spdbccc.job.manager.db.entity.JmcTaskInfo;
import com.spdbccc.job.manager.core.service.job.ShellTaskJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
//@ConditionalOnProperty(name = "spring.profiles.active", havingValue = "pro")
public class ApplicationReadyListener implements ApplicationListener<ApplicationReadyEvent> {
    private final Logger logger = LoggerFactory.getLogger(ApplicationReadyListener.class);

    @Autowired
    private JmcTaskInfoMapper jmcTaskInfoMapper;
    @Autowired
    private JmcJobParamsMapper jmcJobParamsMapper;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        logger.warn("Starting necessary task");
        // 启动任务调度
        startSchedule();
        logger.warn("Starting necessary task end");
    }

    private void startSchedule() {
        List<JmcTaskInfo> jmcTaskInfos = jmcTaskInfoMapper.selectAllJmcTaskInfoByIsCron("1");
        JmcJobParams jmcJobParams = jmcJobParamsMapper.selectByPrimaryKey(1L);
        for (JmcTaskInfo jmcTaskInfo : jmcTaskInfos) {
            ShellTaskJob.build(
                    new TaskEntity(jmcTaskInfo.getId(),jmcTaskInfo.getJobId(),jmcTaskInfo.getTaskName(),jmcTaskInfo.getCommand(),jmcTaskInfo.getIsCron(),
                            jmcTaskInfo.getCron(),jmcTaskInfo.getPriority()),
                    new ParamsEntity(jmcJobParams.getLogPrefix(),jmcJobParams.getNode(),jmcJobParams.getUsername(),jmcJobParams.getPassword(),jmcJobParams.getYarnUrl(),
                            jmcJobParams.getQueue()
                            ));
        }
    }
}
