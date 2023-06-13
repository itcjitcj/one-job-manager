package com.spdbccc.job.manager.db.entity;

import lombok.Data;

import java.util.Date;
@Data
public class JmcTaskLog {
    private Long id;

    private String taskStatus;

    private Long taskid;

    private String applicationId;

    private String command;

    private String createUser;

    private Date createTime;

    private String updateUser;

    private Date updateTime;

    private String isDeleted;

    public JmcTaskLog(String taskStatus, Long taskid, String applicationId, String command) {
        this.taskStatus = taskStatus;
        this.taskid = taskid;
        this.applicationId = applicationId;
        this.command = command;
    }
}