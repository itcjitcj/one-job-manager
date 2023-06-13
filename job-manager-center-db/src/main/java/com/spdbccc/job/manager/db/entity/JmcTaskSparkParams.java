package com.spdbccc.job.manager.db.entity;

import lombok.Data;

import java.util.Date;
@Data
public class JmcTaskSparkParams {
    private Long id;

    private String sparkSubmit;

    private String propertiesFile;

    private Byte master;

    private String deployMode;

    private String queue;

    private String principal;

    private String keytab;

    private String files;

    private String driverMemory;

    private String driverCores;

    private String numExecutors;

    private String executorMemory;

    private String executorCores;

    private String conf;

    private String classUrl;

    private String appName;

    private String jarName;

    private String getYarnLog;

    private String createUser;

    private Date createTime;

    private String updateUser;

    private Date updateTime;

    private String isDeleted;
}