package com.spdbccc.job.manager.db.entity;

import lombok.Data;

import java.util.Date;
@Data
public class JmcTaskInfo {
    private Long id;

    private Long jobId;

    private String taskName;

    private Byte priority;

    private String command;

    private String isCron;

    private String cron;

    private String createUser;

    private Date createTime;

    private String updateUser;

    private Date updateTime;

    private String isDeleted;
}