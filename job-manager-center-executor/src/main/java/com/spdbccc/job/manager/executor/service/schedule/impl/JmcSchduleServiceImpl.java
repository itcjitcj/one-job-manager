package com.spdbccc.job.manager.executor.service.schedule.impl;

import com.spdbccc.job.manager.core.common.Constant;
import com.spdbccc.job.manager.core.common.entity.job.ParamsEntity;
import com.spdbccc.job.manager.core.common.entity.job.TaskEntity;
import com.spdbccc.job.manager.db.dao.JmcJobParamsMapper;
import com.spdbccc.job.manager.db.dao.JmcTaskInfoMapper;
import com.spdbccc.job.manager.db.entity.JmcJobParams;
import com.spdbccc.job.manager.db.entity.JmcTaskInfo;
import com.spdbccc.job.manager.core.service.job.ShellTaskJob;
import com.spdbccc.job.manager.executor.service.schedule.JmcScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JmcSchduleServiceImpl implements JmcScheduleService {
    @Autowired
    private JmcTaskInfoMapper jmcTaskInfoMapper;
    @Autowired
    private JmcJobParamsMapper jmcJobParamsMapper;

    @Override
    public String stopTask(String taskName) {
        TaskEntity taskEntity = new TaskEntity(taskName);
        ShellTaskJob.stop(taskEntity);
        return "stopTask "+ Constant.Message.RETURN_SUCCESS;
    }
    @Override
    public String flushSchedule(){
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
        return "flushSchedule "+ Constant.Message.RETURN_SUCCESS;
    }

    @Override
    public String deleteSchedule(String taskName) {
        TaskEntity taskEntity = new TaskEntity(taskName);
        ShellTaskJob.delete(taskEntity);
        return "deleteSchedule "+ Constant.Message.RETURN_SUCCESS;
    }
    @Override
    public String submitTask(Long taskId){
        JmcTaskInfo jmcTaskInfo = jmcTaskInfoMapper.selectByPrimaryKey(taskId);
        JmcJobParams jmcJobParams = jmcJobParamsMapper.selectByPrimaryKey(1L);
        ShellTaskJob.build(
                new TaskEntity(jmcTaskInfo.getId(),jmcTaskInfo.getJobId(),jmcTaskInfo.getTaskName()+"-simple",jmcTaskInfo.getCommand(),"0",
                        jmcTaskInfo.getCron(),jmcTaskInfo.getPriority()),
                new ParamsEntity(jmcJobParams.getLogPrefix(),jmcJobParams.getNode(),jmcJobParams.getUsername(),jmcJobParams.getPassword(),jmcJobParams.getYarnUrl(),
                        jmcJobParams.getQueue()
                ));
        return "submitTask "+ Constant.Message.RETURN_SUCCESS;
    }
}
