package com.spdbccc.job.manager.core.service.job;

import cn.hutool.core.lang.Assert;
import com.alibaba.fastjson.JSON;
import com.spdbccc.job.manager.core.common.entity.job.ParamsEntity;
import com.spdbccc.job.manager.core.common.entity.job.TaskEntity;
import com.spdbccc.job.manager.core.service.inter.ShellService;
import com.spdbccc.job.manager.core.common.Constant;
import com.spdbccc.job.manager.core.service.impl.ShellServiceImpl;
import com.spdbccc.job.manager.core.util.SchedulerUtils;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.InterruptableJob;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Objects;

@DisallowConcurrentExecution
public class ShellTaskJob extends QuartzJobBean implements InterruptableJob {
    private final ShellService shellService = new ShellServiceImpl();

    private Thread thread;
    private JobExecutionContext context;
    private volatile boolean commandFinish = false;
    private volatile boolean interrupted = false;


    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) {
        context = jobExecutionContext;
        thread = Thread.currentThread();
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        TaskEntity taskEntity = JSON.parseObject(dataMap.getString("taskEntity"), TaskEntity.class);
        ParamsEntity paramsEntity = JSON.parseObject(dataMap.getString("paramsEntity"), ParamsEntity.class);
        shellService.runShell(taskEntity, paramsEntity);
        commandFinish = true;
    }


    public static void build(TaskEntity taskEntity, ParamsEntity paramsEntity) {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("taskEntity", JSON.toJSONString(taskEntity));
        jobDataMap.put("paramsEntity", JSON.toJSONString(paramsEntity));
        if (Objects.equals(taskEntity.getIsCron(), "1")) {
            SchedulerUtils.scheduleCronJob(ShellTaskJob.class, taskEntity.getTaskName(), Constant.JobGroup.SCRIPT_JOB, taskEntity.getCron(), jobDataMap,
                    taskEntity.getPriority());
        } else {
            SchedulerUtils.scheduleSimpleJob(ShellTaskJob.class, taskEntity.getTaskName(), Constant.JobGroup.SCRIPT_JOB,
                    0, 0, jobDataMap, taskEntity.getPriority());
        }
    }

    public static void stop(TaskEntity taskEntity) {
        SchedulerUtils.interrupt(taskEntity.getTaskName(), Constant.JobGroup.SCRIPT_JOB);
    }

    public static void delete(TaskEntity taskEntity) {
        SchedulerUtils.deleteJob(taskEntity.getTaskName(), Constant.JobGroup.SCRIPT_JOB);
    }

    @Override
    public void interrupt() {
        if (!commandFinish && !interrupted) {
            JobDataMap dataMap = context.getJobDetail().getJobDataMap();
            TaskEntity taskEntity = JSON.parseObject(dataMap.getString("taskEntity"), TaskEntity.class);
            ParamsEntity paramsEntity = JSON.parseObject(dataMap.getString("paramsEntity"), ParamsEntity.class);
            Assert.isTrue(shellService.stopShell(taskEntity, paramsEntity),"停止shell任务失败");
            thread.interrupt();
//            SchedulerUtils.deleteJob(taskEntity.getTaskName(), Constant.JobGroup.SCRIPT_JOB);
            interrupted = true;
        }
    }


}
