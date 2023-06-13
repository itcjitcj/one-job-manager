package com.spdbccc.job.manager.core.common.entity.job;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskEntity {
    private Long taskId;
    private Long jobId;
    private String taskName;
    private String command;
    private String isCron;
    private String cron;

    private Long taskLogId;
    private String applicationId;
    private Byte priority;

    private boolean getYarnLog=false;

    public TaskEntity(Long taskId, Long jobId, String taskName, String command,String applicationId) {
        this.taskId = taskId;
        this.jobId = jobId;
        this.taskName = taskName;
        this.command = command;
        this.applicationId = applicationId;
    }

    public TaskEntity(Long taskId, Long jobId, String taskName) {
        this.taskId = taskId;
        this.jobId = jobId;
        this.taskName = taskName;
        this.command = command;
    }

    public TaskEntity(Long taskId, Long jobId, String taskName,String command, Byte priority) {
        this.taskId = taskId;
        this.jobId = jobId;
        this.taskName = taskName;
        this.command = command;
        this.priority = priority;
    }

    public TaskEntity(Long taskId, Long jobId, String taskName, String command, String isCron, String cron, Byte priority) {
        this.taskId = taskId;
        this.jobId = jobId;
        this.taskName = taskName;
        this.command = command;
        this.isCron = isCron;
        this.cron = cron;
        this.priority = priority;
    }

    public TaskEntity(Long taskId, Long jobId, String taskName, String command, String isCron, String cron, Byte priority, boolean getYarnLog) {
        this.taskId = taskId;
        this.jobId = jobId;
        this.taskName = taskName;
        this.command = command;
        this.isCron = isCron;
        this.cron = cron;
        this.priority = priority;
        this.getYarnLog = getYarnLog;
    }

    public TaskEntity(String taskName) {
        this.taskName = taskName;
    }
}
